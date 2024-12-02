package api.mcnc.surveyrespondentservice.client.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 5:15
 */
@Service
@RequiredArgsConstructor
public class SurveyValidationService {
  private final SurveyValidationClient surveyValidationClient;

  public boolean validateExistAndStatus(String surveyId) {
    return surveyValidationClient.validateSurvey(surveyId);
  }
}
