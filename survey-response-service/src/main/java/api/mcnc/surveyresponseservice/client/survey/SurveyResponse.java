package api.mcnc.surveyresponseservice.client.survey;

import api.mcnc.surveyresponseservice.client.survey.response.Survey;

/**
 * 설문 정보
 *
 * @author :유희준
 * @since :2024-11-27 오후 2:17
 */
public interface SurveyResponse {
  Survey getSurvey(String surveyId);
}
