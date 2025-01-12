package api.mcnc.surveyadminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 이메일 서비스로 요청 보내는 클라이언트
 *
 * @author :유희준
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

  // 인증 검증이 됐는지 검증
  @PostMapping("/emails/check-valid")
  boolean isValidDeleteCodes(@RequestParam String email);

  // 비밀번호 초기화 메일
  @PostMapping("/emails/PW")
  ResponseEntity<String> sendPWEmails(@RequestBody Map<String, Object> request);
}
