package api.mcnc.surveyrespondentservice.client.oauth.kakao;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthClientService;
import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 카카오 요청 서비스
 *
 * @author :유희준
 * @since :2024-11-21 오후 9:18
 */
@Service
@RequiredArgsConstructor
public class KakaoClientService implements OAuthClientService {

  private final KakaoOAuthClient kakaoOAuthClient;
  private final KakaoUserClient kakaoUserClient;

  @Value("${kakao.client-id}")
  private String kakaoClientId;

  @Value("${kakao.client-secret}")
  private String kakaoClientSecret;

  @Value("${kakao.redirect-uri}")
  private String kakaoRedirectUri;

  public OAuthToken getToken(String code) {
    return kakaoOAuthClient.requestGetToken(
      kakaoClientId,
      kakaoRedirectUri,
      kakaoClientSecret,
      code
    );
  }

  public KakaoUserInfo getUserInfo(String accessToken) {
    return kakaoUserClient.getUserInfo("Bearer " + accessToken);
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

}
