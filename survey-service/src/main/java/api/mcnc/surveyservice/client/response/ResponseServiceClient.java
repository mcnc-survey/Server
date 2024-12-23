package api.mcnc.surveyservice.client.response;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오후 7:52
 */
@FeignClient(name = "survey-response-service")
public interface ResponseServiceClient {
  @DeleteMapping("/deletion/responses")
  void deleteResponse(List<String> surveyIdList);
}
