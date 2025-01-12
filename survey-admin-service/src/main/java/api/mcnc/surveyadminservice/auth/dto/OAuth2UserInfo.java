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


/**
 * Oauth2로 받아온 유저 정보
 * @author :유희준
 * @since :2024-11-26 오전 11:24
 */
@Builder
public class OAuth2UserInfo {

  @Getter private String name;
  @Getter @EmailEncryption
  private String email;
  @Getter private String phoneNumber;
  @Getter private String provider;

  /**
   * 이메일 AES 암호화
   * @param encryptedEmail AES 암호화된 이메일
   */
  public void updateEncryptedEmail(String encryptedEmail) {
    this.email = encryptedEmail;
  }

  /**
   * registrationId로 로그인 유형에 맞는 OAuth2UserInfo 객체 생성
   * @param registrationId : 소셜 로그인 서비스
   * @param attributes : 소셜 로그인 서비스로부터 받은 유저 정보
   * @return {@link #OAuth2UserInfo}
   */
  public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
    return switch (registrationId) {
      case "google" -> ofGoogle(attributes);
      case "kakao" -> ofKakao(attributes);
      case "naver" -> ofNaver(attributes);
      default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
    };
  }

  /**
   * 구글 로그인 OAuth2UserInfo 객체 생성
   * @param attributes : 소셜 로그인 서비스로부터 받은 유저 정보
   * @return {@link #OAuth2UserInfo}
   */
  private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
    return OAuth2UserInfo.builder()
      .name((String) attributes.get("name"))
      .email((String) attributes.get("email"))
      .phoneNumber((String) attributes.get("phone_number"))
      .provider(GOOGLE)
      .build();
  }

  /**
   * 카카오 로그인 OAuth2UserInfo 객체 생성
   * @param attributes : 소셜 로그인 서비스로부터 받은 유저 정보
   * @return {@link #OAuth2UserInfo}
   */
  private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

    return OAuth2UserInfo.builder()
      .name((String) account.get("name"))
      .email((String) account.get("email"))
      .phoneNumber((String) account.get("phone_number"))
      .provider(KAKAO)
      .build();
  }

  /**
   * 네이버 로그인 OAuth2UserInfo 객체 생성
   * @param attributes : 소셜 로그인 서비스로부터 받은 유저 정보
   * @return {@link #OAuth2UserInfo}
   */
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
