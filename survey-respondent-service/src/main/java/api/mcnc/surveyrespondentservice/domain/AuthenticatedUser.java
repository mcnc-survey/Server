package api.mcnc.surveyrespondentservice.domain;

import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
  @NotBlank
  String name,
  @Email
  String email,
  @NotBlank
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
