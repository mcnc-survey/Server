package api.mcnc.surveyrespondentservice.domain;

import api.mcnc.surveyrespondentservice.client.oauth.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 4:43
 */
@Builder(access = AccessLevel.PRIVATE)
public record AuthenticatedUser(
  String name,
  String email,
  String phoneNumber,
  String provider
) {
  public static AuthenticatedUser of(UserInfo userInfo, String provider) {
    return AuthenticatedUser.builder()
      .name(userInfo.name())
      .email(userInfo.email())
      .phoneNumber(userInfo.phoneNumber())
      .provider(provider)
      .build();
  }
}
