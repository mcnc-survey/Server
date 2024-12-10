package api.mcnc.surveyresponseservice.client.error;

import api.mcnc.surveyresponseservice.client.survey.SurveyClient;
import api.mcnc.surveyresponseservice.client.survey.response.SurveyDetailsResponse;
import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.common.result.Api;
import org.springframework.stereotype.Component;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-10 오전 10:39
 */
@Component
public class SurveyClientFallback implements SurveyClient {
  @Override
  public Api<SurveyDetailsResponse> getSurveyDetails(String surveyId) {
    throw new ResponseException(ResponseErrorCode.NOT_FOUND_SURVEY);
  }

  @Override
  public Boolean validateSurvey(String surveyId) {
    return null;
  }
}
