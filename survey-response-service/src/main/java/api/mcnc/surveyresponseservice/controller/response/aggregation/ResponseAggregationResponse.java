package api.mcnc.surveyresponseservice.controller.response.aggregation;

import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 8:07
 */
public record ResponseAggregationResponse(
  SurveySummary surveySummary,
  Map<Integer, SurveyResultValue> surveyResults
) { }
