package api.mcnc.surveyrespondentservice.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 이메일 인증 번호 확인 요청
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
