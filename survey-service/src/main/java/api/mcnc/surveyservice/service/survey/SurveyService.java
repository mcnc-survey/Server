package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.repository.survey.InsertSurveyAndQuestionListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 10:56
 */
@Service
@RequiredArgsConstructor
public class SurveyService {
  private final InsertSurveyAndQuestionListRepository insertSurveyAndQuestionListRepository;
  private final RequestedByProvider provider;

//  @Transactional
  public void setSurveyAndQuestions(SurveyCreateRequest surveyCreateRequest) {
    String adminId = provider.requestedBy().orElse("SYSTEM");
    String title = surveyCreateRequest.title();
    String description = surveyCreateRequest.description();
    LocalDateTime startAt = surveyCreateRequest.startAt();
    LocalDateTime endAt = surveyCreateRequest.endAt();
    Survey survey = Survey.fromRequest(adminId, title, description, startAt, endAt);

    List<QuestionCreateRequest> questionCreateRequestList = surveyCreateRequest.questions();
    List<Question> questionList = questionCreateRequestList.stream().map(Question::fromRequest).toList();

    insertSurveyAndQuestionListRepository.createSurvey(survey, questionList);
  }




}
