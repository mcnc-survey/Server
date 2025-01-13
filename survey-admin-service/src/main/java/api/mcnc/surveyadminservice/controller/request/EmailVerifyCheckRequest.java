package api.mcnc.surveyadminservice.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 이메일 인증 번호 검증 요청 객체
 *
 * @author :유희준
 * @since :2024-12-19 오전 11:25
 */
public record EmailVerifyCheckRequest(
  @Email
  String email,
  @NotBlank
  String code
) {
}
