package api.mcnc.surveyrespondentservice.client.survey;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 5:10
 */
@FeignClient(name = "SURVEY-SERVICE")
public interface SurveyValidationClient {

  @GetMapping("/survey/validate")
  Boolean validateSurvey(@RequestParam("surveyId") String surveyId);

}
