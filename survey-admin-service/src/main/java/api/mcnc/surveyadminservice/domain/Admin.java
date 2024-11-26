package api.mcnc.surveyadminservice.domain;

import api.mcnc.surveyadminservice.auth.dto.OAuth2UserInfo;
import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import api.mcnc.surveyadminservice.entity.admin.AdminRole;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

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
      .name(oAuth2UserInfo.name())
      .email(oAuth2UserInfo.email())
      .role(AdminRole.ADMIN)
      .password(new BCryptPasswordEncoder().encode("{admin}password"))
      .provider(oAuth2UserInfo.provider())
      .build();
  }
}
