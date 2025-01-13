package api.mcnc.surveyrespondentservice.client.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 설문 검증 서비스
 *
 * @author :유희준
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
