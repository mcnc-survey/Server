package api.mcnc.surveyrespondentservice.client.oauth.naver;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 네이버 oauth 요청
 *
 * @author :유희준
 * @since :2024-11-21 오후 10:13
 */
@FeignClient(
  name = "naverOauthClient",
  url = "${naver.token-uri}?grant_type=authorization_code"
)
public interface NaverOauthClient {
  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  OAuthToken requestGetToken(
    @RequestParam("client_id") String clientId,
    @RequestParam("redirect_uri") String redirectUri,
    @RequestParam("client_secret") String clientSecret,
    @RequestParam("code") String code
  );
}
