package api.mcnc.surveyresponseservice.client.survey;

import api.mcnc.surveyresponseservice.client.survey.response.SurveyDetailsResponse;
import api.mcnc.surveyresponseservice.common.result.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 11:21
 */
@FeignClient(name = "survey", url = "${api.survey-url}")
public interface SurveyClient {
  @GetMapping("/survey-validate/{surveyId}")
  Boolean validateSurvey(@PathVariable("surveyId") String surveyId);

  @GetMapping("/surveys/survey-id/{surveyId}")
  Api<SurveyDetailsResponse> getSurveyDetails(@PathVariable("surveyId") String surveyId);

}
