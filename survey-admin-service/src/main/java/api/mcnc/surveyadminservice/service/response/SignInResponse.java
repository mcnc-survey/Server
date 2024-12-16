package api.mcnc.surveyadminservice.service.response;

import api.mcnc.surveyadminservice.domain.Token;
import lombok.AccessLevel;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오후 2:58
 */
@Builder(access = AccessLevel.PRIVATE)
public record SignInResponse(
  String userName,
  String accessToken,
  String refreshToken
) {
  public static SignInResponse of(String userName, Token token) {
    return SignInResponse.builder()
      .userName(userName)
      .accessToken(token.accessToken())
      .refreshToken(token.refreshToken())
      .build();
  }
}
