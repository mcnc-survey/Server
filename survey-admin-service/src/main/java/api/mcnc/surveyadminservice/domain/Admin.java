package api.mcnc.surveyadminservice.domain;

import api.mcnc.surveyadminservice.auth.dto.OAuth2UserInfo;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.entity.admin.AdminRole;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static api.mcnc.surveyadminservice.common.constants.Provider.EMAIL;
import static api.mcnc.surveyadminservice.entity.admin.AdminRole.ADMIN;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:22
 */
@Builder
public record Admin(
  String id,
  String name,
  String email,
  String password,
  AdminRole role,
  String provider
) {

  public static Admin from(OAuth2UserInfo oAuth2UserInfo) {
    return Admin.builder()
      .name(oAuth2UserInfo.getName())
      .email(oAuth2UserInfo.getEmail())
      .role(ADMIN)
      .password(new BCryptPasswordEncoder().encode("{admin}password"))
      .provider(oAuth2UserInfo.getProvider())
      .build();
  }

  public static Admin from(AdminSignUpRequest request) {
    return Admin.builder()
      .name(request.getUserName())
      .email(request.getEmail())
      .role(ADMIN)
      .password(request.getPassword())
      .provider(EMAIL)
      .build();
  }

}
