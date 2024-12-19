package api.mcnc.surveyservice.client.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-18 오후 4:12
 */
@FeignClient(name = "survey-email-service")
public interface EmailClient {

  // 초대 메일
  @PostMapping("/emails/invite")
  ResponseEntity<String> sendHtmlVerificationEmails(@RequestBody Map<String, Object> request);

}
