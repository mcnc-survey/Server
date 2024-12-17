package api.mcnc.surveyadminservice.controller.request;

import jakarta.validation.constraints.NotBlank;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오후 8:01
 */
public record TokenValidateRequest(
  @NotBlank
  String accessToken
) {
}
