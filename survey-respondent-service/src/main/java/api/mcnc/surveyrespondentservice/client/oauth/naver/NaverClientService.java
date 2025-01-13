package api.mcnc.surveyrespondentservice.client.oauth.naver;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthClientService;
import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 네이버 요청 서비스
 *
 * @author :유희준
 * @since :2024-11-21 오후 10:12
 */
@Service
@RequiredArgsConstructor
public class NaverClientService implements OAuthClientService {

  private final NaverOauthClient naverOauthClient;
  private final NaverUsereClient naverUsereClient;

  @Value("${naver.client-id}")
  private String clientId;
  @Value("${naver.client-secret}")
  private String clientSecret;
  @Value("${naver.redirect-uri}")
  private String naverRedirectUri;

  public OAuthToken getToken(String code) {
    return naverOauthClient.requestGetToken(
      clientId,
      naverRedirectUri,
      clientSecret,
      code
    );
  }

  public NaverUserInfo getUserInfo(String accessToken) {
    return naverUsereClient.getUserInfo("Bearer " + accessToken);
  }

  @Override
  public String getProvider() {
    return "naver";
  }

}
