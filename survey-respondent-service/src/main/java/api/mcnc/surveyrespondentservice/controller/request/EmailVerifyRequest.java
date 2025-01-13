package api.mcnc.surveyrespondentservice.controller.request;

import jakarta.validation.constraints.Email;

/**
 * 이메일 검증 요청
 *
 * @author :유희준
 * @since :2024-12-19 오전 11:03
 */
public record EmailVerifyRequest(
  @Email
  String email
) {
}
