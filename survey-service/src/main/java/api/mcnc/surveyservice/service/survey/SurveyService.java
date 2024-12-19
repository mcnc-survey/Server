package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.admin.AdminServiceClientService;
import api.mcnc.surveyservice.client.email.EmailClientService;
import api.mcnc.surveyservice.client.response.ResponseServiceClientService;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static api.mcnc.surveyservice.common.enums.SurveyErrorCode.INVALID_REQUEST;
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
  private final AdminServiceClientService adminServiceClientService;
  private final EmailClientService emailClientService;

  private final SurveyValidator surveyValidator;

  // 설문 저장
  public String setSurveyAndQuestions(SurveyCreateRequest surveyCreateRequest) {
    String adminId = getAdminId();

    List<QuestionCreateRequest> questionCreateRequestList = surveyCreateRequest.questions();
    // 객체 유효성 검사
    surveyValidator.validateResponses(questionCreateRequestList);

    List<Question> questionList = questionCreateRequestList.stream().map(Question::fromRequest).toList();

    String title = surveyCreateRequest.title();
    String description = surveyCreateRequest.description();
    LocalDateTime startAt = surveyCreateRequest.startAt();
    LocalDateTime endAt = surveyCreateRequest.endAt();

    if(startAt.isAfter(endAt)) {
      throw new SurveyException(START_TIME_MUST_BE_BEFORE_END_TIME);
    }

    Survey survey = Survey.fromRequest(adminId, title, description, startAt, endAt);

    return insertSurveyAndQuestionListRepository.createSurvey(survey, questionList);
  }

  // 작성한 설문 전체 조회
  public List<SurveyResponse> getSurveyList() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllByAdminId(adminId).stream().map(Survey::toResponse).toList();
  }

  public List<SurveyCalendarResponse> getSurveyListForCalendar() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllByAdminId(adminId).stream().map(Survey::toCalendarResponse).toList();
  }

  public void deleteSurveyList(List<String> surveyIds) {
    String adminId = getAdminId();

    responseServiceClientService.deleteResponse(surveyIds);

    deleteSurveyRepository.deleteSurveyList(adminId, surveyIds);
  }

  public void restoreSurveyList(List<String> surveyIds) {
    String adminId = getAdminId();

    updateSurveyStatusRepository.updateSurveyStatusToRestore(adminId, surveyIds);
  }

  public List<SurveyResponse> getSurveyListForDelete() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllByAdminIdForDelete(adminId).stream().map(Survey::toResponse).toList();
  }

  public List<SurveyLikeResponse> getSurveyLikeList() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllLikeSurveyByAdminId(adminId).stream().map(Survey::toLikeResponse).toList();
  }

  // 설문 수정을 위한 상세 보기
  public SurveyDetailsResponse getDetailForEdit(String surveyId) {
    Survey survey = this.getSurvey(surveyId);
    SurveyDetailsResponse surveyDetail = survey.toDetailsResponse();
    updateSurveyStatusRepository.updateSurveyStatusToBeginEdit(surveyId);
    return surveyDetail;
  }

  // 응답을 위한 상세 보기
  public SurveyDetailsResponse getDetail(String surveyId) {
    // TODO: 현재는 설문 아이디만 있으면 다 조회 가능 - 추 후에 수정 필요
    Survey survey = fetchSurveyRepository.fetchBySurveyId(surveyId)
      .orElseThrow(() -> new SurveyException(SurveyErrorCode.FOUND_NOT_SURVEY));
    return survey.toDetailsResponse();
  }

  // 설문 수정
  public void updateSurvey(String surveyId, SurveyUpdateRequest surveyUpdateRequest) {
    Survey survey = this.getSurvey(surveyId);

    List<SurveyUpdateRequest.Question> questions = surveyUpdateRequest.updateQuestionList();
    // 유효성 검사
    surveyValidator.validateResponseUpdates(questions);

    // EDIT 상태의 설문에 대한 요청이 아니면 exception
    if (!SurveyStatus.EDIT.equals(survey.status())){
      throw new SurveyException(INVALID_REQUEST, "수정 가능한 설문이 아닙니다.");
    }

    // 종료 날짜에 따른 변경해야 할 상태 - 날짜 검증 때문에 udpate보다 앞에
    SurveyStatus changedStatus = this.calculateTime(surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());

    // 설문 수정

    // id가 존재하면 수정 , id 없으면 추가
    Map<Boolean, List<Question>> partitioned = questions.stream()
      .map(Question::fromRequest)
      .collect(Collectors.partitioningBy(
        question -> question.id() != null && !question.id().isBlank()
      ));

    Survey updateSurvey = Survey.fromRequest(survey.adminId(), surveyUpdateRequest.title(), surveyUpdateRequest.description(), surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());
    List<Question> withId = partitioned.get(true);
    List<Question> withoutId = partitioned.get(false);
    Set<String> updateIds = withId.stream().map(Question::id).collect(Collectors.toSet());

    // 수정
    updateSurveyRepository.updateSurvey(surveyId, updateSurvey, withId, withoutId, updateIds);

    // 수정 완료 하고 상태 변경
    updateSurveyStatusRepository.updateSurveyStatusToEndEdit(surveyId, changedStatus);
  }

  // 설문 삭제
  public void deleteSurvey(String surveyId) {
    Survey survey = this.getSurvey(surveyId);
    updateSurveyStatusRepository.updateSurveyStatusToDelete(surveyId);
  }

  public void like(String surveyId) {
    getAdminId();
    updateSurveyRepository.updateLike(surveyId);

  }

  public void invite(String surveyId, List<String> emails) {
    Survey survey = getSurvey(surveyId);
    emailClientService.sendHtmlVerificationEmails(survey.title(), survey.surveyLink(),emails);
  }

  // ==============================
  // private method
  // ==============================

  // 시간 계산
  private SurveyStatus calculateTime(LocalDateTime editStart, LocalDateTime editEnd) {
    if(editStart.isAfter(editEnd)) {
      throw new SurveyException(START_TIME_MUST_BE_BEFORE_END_TIME);
    }
    LocalDateTime now = LocalDateTime.now();
    if (now.isBefore(editStart)) {
      return SurveyStatus.WAIT;
    } else if (now.isAfter(editEnd)) {
      return SurveyStatus.END;
    } else {
      return SurveyStatus.ON;
    }
  }

  // 설문 아이디와 작성자 아이디로 설문 조회
  private Survey getSurvey(String surveyId) {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchBySurveyIdAndAdminId(surveyId, adminId)
      .orElseThrow(() -> new SurveyException(INVALID_REQUEST, "설문이 존재하지 않습니다."));
  }

  // 작성자 아이디 가져오기
  private final RequestedByProvider provider;
  private String getAdminId() {
    String adminId = provider.requestedBy().orElse("SYSTEM");
    boolean isExistAdmin = adminServiceClientService.isExistAdmin(adminId);
    if (!isExistAdmin) {
      throw new SurveyException(INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다.");
    }
    return adminId;
  }

}
