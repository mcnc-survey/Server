package api.mcnc.surveyrespondentservice.controller.request;

import jakarta.validation.constraints.Email;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오전 11:03
 */
public record EmailVerifyRequest(
  @Email
  String email
) {
}
