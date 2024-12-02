package api.mcnc.surveyresponseservice.service.response;

import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 8:07
 */
public record ResponseAggregationResponse(
  Integer totalResponseCount,
  Map<Integer, Object> groupByOrderNumber
) { }
