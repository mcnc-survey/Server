package api.mcnc.surveyadminservice.service.response;

import api.mcnc.surveyadminservice.domain.Token;
import lombok.AccessLevel;
import lombok.Builder;

/**
 * 응답
 *
 * @author :유희준
 * @since :2024-12-16 오후 2:58
 */
@Builder(access = AccessLevel.PRIVATE)
public record AdminSignInResponse(
  String userName,
  String accessToken,
  String refreshToken
) {
  public static AdminSignInResponse of(String userName, Token token) {
    return AdminSignInResponse.builder()
      .userName(userName)
      .accessToken(token.accessToken())
      .refreshToken(token.refreshToken())
      .build();
  }
}
