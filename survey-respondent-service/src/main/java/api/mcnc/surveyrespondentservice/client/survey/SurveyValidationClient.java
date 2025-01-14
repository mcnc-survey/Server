package api.mcnc.surveyrespondentservice.client.survey;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 설문 존재 검증 open feign
 *
 * @author :유희준
 * @since :2024-11-22 오후 5:10
 */
@FeignClient(name = "SURVEY-SERVICE")
public interface SurveyValidationClient {

  @GetMapping("/survey-validate/{surveyId}")
  Boolean validateSurvey(@PathVariable("surveyId") String surveyId);

}
