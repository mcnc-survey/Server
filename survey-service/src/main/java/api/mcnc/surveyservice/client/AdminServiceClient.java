package api.mcnc.surveyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:20
 */
@FeignClient(name = "SURVEY-ADMIN-SERVICE")
public interface AdminServiceClient {

  @GetMapping("/admin-validation/{adminId}")
  boolean adminValidation(@PathVariable String adminId);

}
