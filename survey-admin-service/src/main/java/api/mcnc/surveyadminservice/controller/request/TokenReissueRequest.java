package api.mcnc.surveyadminservice.controller.request;

import jakarta.validation.constraints.NotBlank;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오전 12:02
 */
public record TokenReissueRequest(
  @NotBlank
  String accessToken
) {
}
