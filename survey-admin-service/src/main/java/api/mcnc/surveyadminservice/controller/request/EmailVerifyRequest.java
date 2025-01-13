package api.mcnc.surveyadminservice.controller.request;

import jakarta.validation.constraints.Email;

/**
 * 이메일 인증 요청 객체
 *
 * @author :유희준
 * @since :2024-12-19 오전 11:03
 */
public record EmailVerifyRequest(
  @Email
  String email
) {
}
