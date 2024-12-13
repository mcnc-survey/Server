package api.mcnc.surveyadminservice.controller.response;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-13 오후 12:58
 */
@Builder
public record AdminSignUpResponse(
  String id,
  String name,
  String email
) {
}
