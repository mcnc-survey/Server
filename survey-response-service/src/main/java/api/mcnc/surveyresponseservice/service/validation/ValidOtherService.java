package api.mcnc.surveyresponseservice.service.validation;

import api.mcnc.surveyresponseservice.client.respondent.RespondentValidate;
import api.mcnc.surveyresponseservice.client.survey.SurveyResponse;
import api.mcnc.surveyresponseservice.client.survey.SurveyValidate;
import api.mcnc.surveyresponseservice.client.survey.response.Survey;
import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-26 오후 11:29
 */
@Component
@RequiredArgsConstructor
public class ValidOtherService {

  private final RespondentValidate respondentValidate;
  private final SurveyValidate surveyValidate;
  private final SurveyResponse surveyResponse;

  public void validate(String respondentId, String surveyId) {
    this.validateRespondent(respondentId);
    this.validateSurvey(surveyId);
  }

  public void validateRespondent(String respondentId) {
    if (!respondentValidate.isExistRespondent(respondentId)) {
      throw new ResponseException(ResponseErrorCode.NOT_EXIST_RESPONDENT);
    }
  }

  public void validateSurvey(String surveyId) {
    if (!surveyValidate.isExistSurvey(surveyId)) {
      throw new ResponseException(ResponseErrorCode.NOT_EXIST_SURVEY);
    }
  }

  public Survey validateAndGetSurvey(String surveyId) {
    return surveyResponse.getSurvey(surveyId);
  }

}
