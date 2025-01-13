package api.mcnc.surveyresponseservice.client.survey;

/**
 * 설문 유효성
 *
 * @author :유희준
 * @since :2024-11-26 오후 11:23
 */
public interface SurveyValidate {
  boolean isExistSurvey(String surveyId);
}
