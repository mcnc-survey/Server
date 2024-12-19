package api.mcnc.surveyrespondentservice.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오전 11:25
 */
public record EmailVerifyCheckRequest(
  @Email
  String email,
  @NotBlank
  String code
) {
}
