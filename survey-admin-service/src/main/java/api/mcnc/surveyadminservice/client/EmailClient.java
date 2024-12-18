package api.mcnc.surveyadminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-17 오후 8:43
 */
@FeignClient(name = "survey-email-service")
public interface EmailClient {
  // 인증 코드 요청 API
  @GetMapping("/emails/request-code")
  String requestVerificationCode(@RequestParam String email);

  // 인증 코드 검증 API
  @PostMapping("/emails/check-code")
  boolean verifyCode(@RequestParam String email, @RequestParam String code);

  // 비밀번호 초기화 메일
  @PostMapping("/emails/PW")
  ResponseEntity<String> sendPWEmails(@RequestBody Map<String, Object> request);
}
