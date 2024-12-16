package api.mcnc.surveyadminservice.domain;

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
}

