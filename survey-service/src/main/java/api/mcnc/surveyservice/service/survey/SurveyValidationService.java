package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.repository.survey.FetchSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:53
 */
@Service
@RequiredArgsConstructor
public class SurveyValidationService {

  private final FetchSurveyRepository fetchSurveyRepository;

  // 설문이 존재하는지
  public boolean existsBySurveyId(String surveyId) {
    return fetchSurveyRepository.existsBySurveyId(surveyId);
  }

  public Survey getSruvey(String surveyId) {
    return fetchSurveyRepository.fetchBySurveyId(surveyId).orElse(null);
  }
}
