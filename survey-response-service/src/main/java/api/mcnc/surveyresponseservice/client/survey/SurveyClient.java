package api.mcnc.surveyresponseservice.client.survey;

import api.mcnc.surveyresponseservice.client.error.decoder.SruveyResponseErrorDecoder;
import api.mcnc.surveyresponseservice.client.error.fallback.SurveyClientFallback;
import api.mcnc.surveyresponseservice.client.survey.response.Survey;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 11:21
 */
@FeignClient(name = "SURVEY-SERVICE", configuration = SruveyResponseErrorDecoder.class, fallback = SurveyClientFallback.class)
public interface SurveyClient {
  @GetMapping("/survey-validate/{surveyId}")
  Boolean validateSurvey(@PathVariable("surveyId") String surveyId);

  @GetMapping("/survey-get/{surveyId}")
  Survey getSurvey(@PathVariable("surveyId") String surveyId);

}
