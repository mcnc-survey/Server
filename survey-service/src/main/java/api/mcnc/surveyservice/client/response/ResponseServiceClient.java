package api.mcnc.surveyservice.client.response;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-19 오후 7:52
 */
@FeignClient(name = "survey-response-service")
public interface ResponseServiceClient {
  @DeleteMapping("/deletion/responses")
  void deleteResponse(@RequestBody List<String> surveyIdList);

  @PutMapping("/update/response")
  void updateResponse(@RequestBody List<ResponseUpdate> responseUpdateList);
}
