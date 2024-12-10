package api.mcnc.surveyresponseservice.client.error.fallback;

import api.mcnc.surveyresponseservice.client.survey.SurveyClient;
import api.mcnc.surveyresponseservice.client.survey.response.Survey;
import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.common.result.Api;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-10 오전 10:39
 */
@Component
public class SurveyClientFallback implements FallbackFactory<SurveyClient> {
  @Override
  public SurveyClient create(Throwable cause) {
    return new SurveyClient() {
      @Override
      public Boolean validateSurvey(String surveyId) {
        return false;
      }

      @Override
      public Survey getSurvey(String surveyId) {
        throw new ResponseException(ResponseErrorCode.NOT_FOUND_SURVEY);
      }
    };
  }
}
