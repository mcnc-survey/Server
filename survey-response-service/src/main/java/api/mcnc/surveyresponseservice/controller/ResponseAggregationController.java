package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.common.enums.SuccessCode;
import api.mcnc.surveyresponseservice.common.result.Api;
import api.mcnc.surveyresponseservice.service.ResponseAggregationService;
import api.mcnc.surveyresponseservice.controller.response.aggregation.ResponseAggregationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 집계 데이터
 *
 * @author :유희준
 * @since :2024-11-15 오후 8:50
 */
@RestController
@RequiredArgsConstructor
public class ResponseAggregationController {

  private final ResponseAggregationService aggregationService;

  @GetMapping("/responses-aggregation/{surveyId}")
  public Api<ResponseAggregationResponse> getResponseAggregationBySurveyId(@PathVariable("surveyId") String surveyId) {
    ResponseAggregationResponse responseAggregationBySurveyId = aggregationService.getResponseAggregationBySurveyId(surveyId);
    return Api.ok(SuccessCode.SUCCESS, responseAggregationBySurveyId);
  }

}
