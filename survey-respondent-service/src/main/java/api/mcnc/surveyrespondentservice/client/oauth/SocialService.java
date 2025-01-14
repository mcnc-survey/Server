package api.mcnc.surveyrespondentservice.client.oauth;

import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode;
import api.mcnc.surveyrespondentservice.common.exception.custom.RespondentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 로그인 방식에 맞는 요청 처리
 *
 * @author :유희준
 * @since :2024-11-22 오후 2:16
 */
@Service
@RequiredArgsConstructor
public class SocialService {
  private final List<OAuthClientService> oauthClientServices;

  public UserInfo getAuthenticatedSocialUserInfo(String provider, String code) {
    OAuthClientService service = extractServiceByProvider(provider);
    String accessToken = service.getToken(code).accessToken();
    return service.getUserInfo(accessToken);
  }

  private OAuthClientService extractServiceByProvider(String provider) {
    return oauthClientServices.stream()
      .filter(service -> service.getProvider().equals(provider))
      .findFirst()
      .orElseThrow(() -> new RespondentException(RespondentErrorCode.INVALID_REQUEST, "google, naver, kakao 인증만 가능합니다"));
  }

}
