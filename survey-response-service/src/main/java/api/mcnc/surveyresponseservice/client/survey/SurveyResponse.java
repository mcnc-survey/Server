package api.mcnc.surveyresponseservice.client.survey;

import api.mcnc.surveyresponseservice.client.survey.response.SurveyDetailsResponse;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:17
 */
public interface SurveyResponse {
  SurveyDetailsResponse getSurveyDetails(String surveyId);
}