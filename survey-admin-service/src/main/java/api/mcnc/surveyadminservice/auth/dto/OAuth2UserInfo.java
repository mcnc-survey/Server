package api.mcnc.surveyadminservice.auth.dto;

import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import api.mcnc.surveyadminservice.common.constants.Provider;
import api.mcnc.surveyadminservice.common.exception.AuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static api.mcnc.surveyadminservice.common.constants.Provider.*;
import static api.mcnc.surveyadminservice.common.enums.AuthErrorCode.ILLEGAL_REGISTRATION_ID;


@Builder
public class OAuth2UserInfo {

  @Getter private String name;
  @Getter @EmailEncryption
  private String email;
  @Getter private String phoneNumber;
  @Getter private String provider;

  public void updateEncryptedEmail(String encryptedEmail) {
    this.email = encryptedEmail;
  }

  public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
    return switch (registrationId) {
      case "google" -> ofGoogle(attributes);
      case "kakao" -> ofKakao(attributes);
      case "naver" -> ofNaver(attributes);
      default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
    };
  }

  private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
    return OAuth2UserInfo.builder()
      .name((String) attributes.get("name"))
      .email((String) attributes.get("email"))
      .phoneNumber((String) attributes.get("phone_number"))
      .provider(GOOGLE)
      .build();
  }

  private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

    return OAuth2UserInfo.builder()
      .name((String) account.get("name"))
      .email((String) account.get("email"))
      .phoneNumber((String) account.get("phone_number"))
      .provider(KAKAO)
      .build();
  }

  private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2UserInfo.builder()
      .name((String) response.get("name"))
      .email((String) response.get("email"))
      .phoneNumber((String) response.get("mobile"))
      .provider(NAVER)
      .build();
  }
}
