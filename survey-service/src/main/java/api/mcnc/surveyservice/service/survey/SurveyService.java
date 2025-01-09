package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.email.EmailClientService;
import api.mcnc.surveyservice.client.notification.NotificationClientService;
import api.mcnc.surveyservice.client.notification.Request;
import api.mcnc.surveyservice.client.response.ResponseServiceClientService;
import api.mcnc.surveyservice.client.response.ResponseUpdate;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyDeleteRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.SurveyCalendarResponse;
import api.mcnc.surveyservice.controller.response.SurveyDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyLikeResponse;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import api.mcnc.surveyservice.repository.survey.*;
import api.mcnc.surveyservice.service.validation.SurveyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static api.mcnc.surveyservice.client.notification.Type.*;
import static api.mcnc.surveyservice.common.enums.SurveyErrorCode.START_TIME_MUST_BE_BEFORE_END_TIME;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 10:56
 */
@Service
@RequiredArgsConstructor
public class SurveyService {
  private final FetchSurveyRepository fetchSurveyRepository;
  private final InsertSurveyAndQuestionListRepository insertSurveyAndQuestionListRepository;
  private final UpdateSurveyRepository updateSurveyRepository;
  private final DeleteSurveyRepository deleteSurveyRepository;

  private final UpdateSurveyStatusRepository updateSurveyStatusRepository;

  private final ResponseServiceClientService responseServiceClientService;
  private final EmailClientService emailClientService;
  private final NotificationClientService notificationClientService;

  private final SurveyValidator surveyValidator;
  private final CacheService cacheService;

  private final RedisTemplate<String, String> redisTemplate;

  // 설문 저장
  public String setSurveyAndQuestions(SurveyCreateRequest surveyCreateRequest) {
    String adminId = cacheService.getAdminId();

    List<QuestionCreateRequest> questionCreateRequestList = surveyCreateRequest.questions();
    // 객체 유효성 검사
    surveyValidator.validateResponses(questionCreateRequestList);

    List<Question> questionList = questionCreateRequestList.stream().map(Question::fromRequest).toList();

    String title = surveyCreateRequest.title();
    String description = surveyCreateRequest.description();
    LocalDateTime startAt = surveyCreateRequest.startAt();
    LocalDateTime endAt = surveyCreateRequest.endAt();

    SurveyStatus surveyStatus = this.calculateTime(startAt, endAt);

    Survey survey = Survey.fromRequest(adminId, title, description, surveyStatus, startAt, endAt);

    String surveyId = insertSurveyAndQuestionListRepository.createSurvey(survey, questionList);
    notificationClientService.publishNotification(Request.of(surveyId, survey.getTitle(), SURVEY_CREATE), adminId);
    return surveyId;
  }

  // 작성한 설문 전체 조회
  public List<SurveyResponse> getSurveyList() {
    String adminId = cacheService.getAdminId();
    return fetchSurveyRepository.fetchAllByAdminId(adminId).stream().map(Survey::toResponse).toList();
  }

  public List<SurveyCalendarResponse> getSurveyListForCalendar() {
    String adminId = cacheService.getAdminId();
    return fetchSurveyRepository.fetchAllByAdminId(adminId).stream().map(Survey::toCalendarResponse).toList();
  }

  public void deleteSurveyList(List<SurveyDeleteRequest.surveyDeleteInfo> surveyInfos) {
    String adminId = cacheService.getAdminId();
    List<String> ids = surveyInfos.stream().map(SurveyDeleteRequest.surveyDeleteInfo::surveyId).toList();
    deleteSurveyRepository.deleteSurveyList(adminId, ids);
    responseServiceClientService.deleteResponse(ids);

    ids.forEach(id -> redisTemplate.delete("survey::survey:id:" + id));

    // 삭제 후 알림
    surveyInfos.forEach(surveyInfo -> {
      notificationClientService.publishNotification(Request.of(surveyInfo.surveyId(), surveyInfo.title(), SURVEY_DELETE), adminId);
    });
  }

  public void restoreSurveyList(List<String> surveyIds) {
    String adminId = cacheService.getAdminId();

    updateSurveyStatusRepository.updateSurveyStatusToRestore(adminId, surveyIds);
  }

  public List<SurveyResponse> getSurveyListForDelete() {
    String adminId = cacheService.getAdminId();
    return fetchSurveyRepository.fetchAllByAdminIdForDelete(adminId).stream().map(Survey::toResponse).toList();
  }

  public List<SurveyLikeResponse> getSurveyLikeList() {
    String adminId = cacheService.getAdminId();
    return fetchSurveyRepository.fetchAllLikeSurveyByAdminId(adminId).stream().map(Survey::toLikeResponse).toList();
  }

  // 설문 수정을 위한 상세 보기
  public SurveyDetailsResponse getDetailForEdit(String surveyId) {
    Survey survey = this.cacheService.getSurvey(surveyId);
    return survey.toDetailsResponse();
  }

  // 응답을 위한 상세 보기
  public SurveyDetailsResponse getDetail(String surveyId) {
    return cacheService.getSurvey(surveyId).toDetailsResponse();
  }

  // 설문 수정
  @CachePut(value = "survey", key = "'survey:id:' + #surveyId",  cacheManager = "surveyCacheManager")
  public Survey updateSurvey(String surveyId, SurveyUpdateRequest surveyUpdateRequest) {
    Survey survey = this.cacheService.getSurvey(surveyId);

    List<SurveyUpdateRequest.Question> questions = surveyUpdateRequest.updateQuestionList();
    // 유효성 검사
    surveyValidator.validateResponseUpdates(questions);

    // 종료 날짜에 따른 변경해야 할 상태 - 날짜 검증 때문에 udpate보다 앞에
    SurveyStatus changedStatus = this.calculateTime(surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());

    // 설문 수정

    // id가 존재하면 수정 , id 없으면 추가
    Map<Boolean, List<Question>> partitioned = questions.stream()
      .map(Question::fromRequest)
      .collect(Collectors.partitioningBy(
        question -> question.getId() != null && !question.getId().isBlank()
      ));

    Survey updateSurvey = Survey.fromRequest(survey.getAdminId(), surveyUpdateRequest.title(), surveyUpdateRequest.description(), changedStatus, surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());
    List<Question> withId = partitioned.get(true);
    List<Question> withoutId = partitioned.get(false);
    Set<String> updateIds = withId.stream().map(Question::getId).collect(Collectors.toSet());

    // 수정
    Survey updatedSurvey = updateSurveyRepository.updateSurvey(surveyId, updateSurvey, withId, withoutId, updateIds);

    if (!withId.isEmpty()) {
      List<ResponseUpdate> updateList = withId.stream()
        .map(question -> ResponseUpdate.of(surveyId, question.getId(), question.getOrder(), question.getQuestionType()))
        .toList();
      responseServiceClientService.updateResponse(updateList);
    }

    // 수정 완료 알림
    notificationClientService.publishNotification(Request.of(surveyId, updateSurvey.getTitle(), SURVEY_EDIT), survey.getAdminId());
    return updatedSurvey;
  }

  // 설문 삭제
  public void deleteSurvey(String surveyId) {
    Survey survey = cacheService.getSurvey(surveyId);
    updateSurveyStatusRepository.updateSurveyStatusToDelete(surveyId);
  }

  public void like(String surveyId) {
    cacheService.getAdminId();
    updateSurveyRepository.updateLike(surveyId);

  }

  public void invite(String surveyId, List<String> emails) {
    Survey survey = cacheService.getSurvey(surveyId);
    emailClientService.sendHtmlVerificationEmails(survey.getTitle(), survey.surveyLink(),emails);
  }

  public boolean isExist(String surveyId) {
    String adminId = cacheService.getAdminId();
    return fetchSurveyRepository.existsBySurveyIdAndAdminId(surveyId, adminId);
  }

  // ==============================
  // private method
  // ==============================

  // 시간 계산
  private SurveyStatus calculateTime(LocalDateTime start, LocalDateTime end) {
    if(start.isAfter(end)) {
      throw new SurveyException(START_TIME_MUST_BE_BEFORE_END_TIME);
    }
    LocalDateTime now = LocalDateTime.now();
    if (now.isBefore(start)) {
      return SurveyStatus.WAIT;
    } else if (now.isAfter(end)) {
      return SurveyStatus.END;
    } else {
      return SurveyStatus.ON;
    }
  }
  // 설문 아이디와 작성자 아이디로 설문 조회


}
