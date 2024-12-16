package api.mcnc.surveyadminservice.domain;

import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 12:55
 */
@Builder
public record Token(
  String id,
  String accessToken,
  String refreshToken
) {
  public TokenResponse toResponse() {
    return new TokenResponse(accessToken);
  }
}

