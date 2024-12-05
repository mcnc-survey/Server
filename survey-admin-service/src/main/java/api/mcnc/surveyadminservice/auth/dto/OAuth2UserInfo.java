package api.mcnc.surveyadminservice.auth.dto;

import api.mcnc.surveyadminservice.common.exception.AuthException;
import lombok.Builder;

import java.util.Map;

import static api.mcnc.surveyadminservice.common.enums.AuthErrorCode.ILLEGAL_REGISTRATION_ID;


@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String phoneNumber,
        String provider
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .phoneNumber((String) attributes.get("phone_number"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .phoneNumber((String) profile.get("phone_number"))
                .build();
    }

}
