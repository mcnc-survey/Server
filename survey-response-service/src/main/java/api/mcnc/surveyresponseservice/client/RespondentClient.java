package api.mcnc.surveyresponseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:20
 */
@FeignClient(name = "SURVEY-RESPONDENT-SERVICE")
public interface RespondentClient {

  @GetMapping("/respondent-validation/{respondentId}")
  boolean validateRespondent(@PathVariable String respondentId);

}
