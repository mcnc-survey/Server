package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.AdminServiceClientService;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
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

  private final AdminServiceClientService adminServiceClientService;

  // 설문 저장
  public void setSurveyAndQuestions(SurveyCreateRequest surveyCreateRequest) {
    String adminId = getAdminId();
    String title = surveyCreateRequest.title();
    String description = surveyCreateRequest.description();
    LocalDateTime startAt = surveyCreateRequest.startAt();
    LocalDateTime endAt = surveyCreateRequest.endAt();

    if(startAt.isAfter(endAt)) {
      throw new SurveyException(START_TIME_MUST_BE_BEFORE_END_TIME);
    }

    Survey survey = Survey.fromRequest(adminId, title, description, startAt, endAt);

    List<QuestionCreateRequest> questionCreateRequestList = surveyCreateRequest.questions();
    List<Question> questionList = questionCreateRequestList.stream().map(Question::fromRequest).toList();

    insertSurveyAndQuestionListRepository.createSurvey(survey, questionList);
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

  public List<SurveyResponse> getSurveyListForDelete() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllByAdminIdForDelete(adminId).stream().map(Survey::toResponse).toList();
  }

  public List<SurveyLikeResponse> getSurveyLikeList() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllLikeSurveyByAdminId(adminId).stream().map(Survey::toLikeResponse).toList();
  }

  // 설문 수정을 위한 상세 보기
  public SurveyDetailsResponse getDetail(String surveyId) {
    Survey survey = this.getSurvey(surveyId);
    SurveyDetailsResponse surveyDetail = survey.toDetailsResponse();
    updateSurveyStatusRepository.updateSurveyStatusToBeginEdit(surveyId);
    return surveyDetail;
  }

  // 설문 수정
  public void updateSurvey(String surveyId, SurveyUpdateRequest surveyUpdateRequest) {
    Survey survey = this.getSurvey(surveyId);


    // EDIT 상태의 설문에 대한 요청이 아니면 exception
    if (!SurveyStatus.EDIT.equals(survey.status())){
      throw new SurveyException(INVALID_REQUEST, "수정 가능한 설문이 아닙니다.");
    }

    // 종료 날짜에 따른 변경해야 할 상태 - 날짜 검증 때문에 udpate보다 앞에
    SurveyStatus changedStatus = this.calculateTime(surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());
    
    // 설문 수정
    List<SurveyUpdateRequest.Question> questions = surveyUpdateRequest.updateQuestionList();

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
    /**
     * 처음 삭제 요청 시 status만 delete로 변경 - 논리적인 삭제
     * delete 상태의 설문에 한 번 더 요청시 데이터 베이스에서 삭제 - 물리적인 삭제
     */

    if (SurveyStatus.DELETE.equals(survey.status())) {
    // 물리적인 삭제
      deleteSurveyRepository.deleteSurvey(surveyId);
    } else {
    // 논리적인 삭제
      updateSurveyStatusRepository.updateSurveyStatusToDelete(surveyId);
    }
  }


  public void like(String surveyId) {
    getAdminId();
    updateSurveyRepository.updateLike(surveyId);

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
