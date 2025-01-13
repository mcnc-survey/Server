package api.mcnc.surveyrespondentservice.client.oauth.google;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthClientService;
import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * google 요청 서비스
 *
 * @author :유희준
 * @since :2024-11-21 오후 10:04
 */
@Service
@RequiredArgsConstructor
public class GoogleClientService implements OAuthClientService {
  private final GoogleOauthClient googleOauthClient;
  private final GoogleUserClient googleUserClient;

  @Value("${google.client-id}")
  private String clientId;
  @Value("${google.client-secret}")
  private String clientSecret;
  @Value("${google.redirect-uri}")
  private String googleRedirectUri;

  public OAuthToken getToken(String code) {
    return googleOauthClient.requestGetToken(
      clientId,
      googleRedirectUri,
      clientSecret,
      code
    );
  }

  public GoogleUserInfo getUserInfo(String accessToken) {
    return googleUserClient.getUserInfo("Bearer " + accessToken);
  }

  @Override
  public String getProvider() {
    return "google";
  }
}
