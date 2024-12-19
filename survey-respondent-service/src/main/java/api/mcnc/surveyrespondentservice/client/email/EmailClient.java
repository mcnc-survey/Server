package api.mcnc.surveyrespondentservice.client.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오전 10:47
 */
@FeignClient(name = "survey-email-service")
public interface EmailClient {
  // 인증 코드 요청 API
  @GetMapping("/emails/request-code")
  String requestVerificationCode(@RequestParam String email);

  // 인증 코드 검증 API
  @PostMapping("/emails/check-code")
  boolean verifyCode(@RequestParam String email, @RequestParam String code);

  // 인증 검증이 됐는지 검증
  @PostMapping("/check-valid")
  boolean isValidDeleteCodes(@RequestParam String email);
}
