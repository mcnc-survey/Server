package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.AdminServiceClientService;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.repository.survey.FetchSurveyRepository;
import api.mcnc.surveyservice.repository.survey.InsertSurveyAndQuestionListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
  private final AdminServiceClientService adminServiceClientService;
  private final RequestedByProvider provider;

//  @Transactional
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


  public List<SurveyResponse> getSurveyList() {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchAllByAdminId(adminId).stream().map(Survey::toResponse).toList();
  }

  private String getAdminId() {
    String adminId = provider.requestedBy().orElse("SYSTEM");
    boolean isExistAdmin = adminServiceClientService.isExistAdmin(adminId);
    if (!isExistAdmin) {
      throw new SurveyException(INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다.");
    }
    return adminId;
  }

  public boolean existsBySurveyId(String surveyId) {
    return fetchSurveyRepository.existsBySurveyId(surveyId);
  }
}
