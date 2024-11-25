package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.AdminServiceClientService;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.SurveyDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import api.mcnc.surveyservice.repository.survey.FetchSurveyRepository;
import api.mcnc.surveyservice.repository.survey.InsertSurveyAndQuestionListRepository;
import api.mcnc.surveyservice.repository.survey.UpdateSurveyRepository;
import api.mcnc.surveyservice.repository.survey.UpdateSurveyStatusRepository;
import jakarta.validation.Valid;
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
  private final UpdateSurveyStatusRepository updateSurveyStatusRepository;
  private final AdminServiceClientService adminServiceClientService;
  private final RequestedByProvider provider;

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

  // 설문 수정을 위한 상세 보기
  public SurveyDetailsResponse getDetail(String surveyId) {
    SurveyDetailsResponse surveyDetail = getSurveyDetail(surveyId);
    updateSurveyStatusRepository.updateSurveyStatusToBeginEdit(surveyId);
    return surveyDetail;
  }

  public void updateSurvey(String surveyId, SurveyUpdateRequest surveyUpdateRequest) {
    String adminId = getAdminId();
    // 내가 작성한 설문인지
    Survey survey = fetchSurveyRepository.fetchBySurveyIdAndAdminId(surveyId, adminId)
      .orElseThrow(() -> new SurveyException(INVALID_REQUEST, "설문이 존재하지 않습니다."));

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

    Survey updateSurvey = Survey.fromRequest(adminId, surveyUpdateRequest.title(), surveyUpdateRequest.description(), surveyUpdateRequest.startAt(), surveyUpdateRequest.endAt());
    List<Question> withId = partitioned.get(true);
    List<Question> withoutId = partitioned.get(false);
    Set<String> updateIds = withId.stream().map(Question::id).collect(Collectors.toSet());

    // 수정
    updateSurveyRepository.updateSurvey(surveyId, updateSurvey, withId, withoutId, updateIds);

    // 수정 완료 하고 상태 변경
    updateSurveyStatusRepository.updateSurveyStatusToEndEdit(surveyId, changedStatus);
  }

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

  // 설문 아이디와 작성자 아이디로 설문 상세 조회
  private SurveyDetailsResponse getSurveyDetail(String surveyId) {
    String adminId = getAdminId();
    Survey survey = fetchSurveyRepository.fetchBySurveyIdAndAdminId(surveyId, adminId)
      .orElseThrow(() -> new SurveyException(INVALID_REQUEST, "설문이 존재하지 않습니다."));
    return survey.toDetailsResponse();
  }

  // 작성자 아이디 가져오기
  private String getAdminId() {
    String adminId = provider.requestedBy().orElse("SYSTEM");
    boolean isExistAdmin = adminServiceClientService.isExistAdmin(adminId);
    if (!isExistAdmin) {
      throw new SurveyException(INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다.");
    }
    return adminId;
  }

}
