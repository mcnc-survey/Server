package api.mcnc.surveyresponseservice.client.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 11:24
 */
@Service
@RequiredArgsConstructor
public class SurveyClientService implements SurveyValidate{

  private final SurveyClient surveyClient;

  @Override
  public boolean isExistSurvey(String surveyId) {
    return surveyClient.validateSurvey(surveyId);
  }
}
