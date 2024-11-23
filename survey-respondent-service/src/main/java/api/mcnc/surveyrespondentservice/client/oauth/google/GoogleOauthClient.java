package api.mcnc.surveyrespondentservice.client.oauth.google;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 5:55
 */
@FeignClient(
  name = "googleFeignClient",
  url = "${google.token-uri}?grant_type=authorization_code"
)
public interface GoogleOauthClient {
  @PostMapping(consumes =  MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  OAuthToken requestGetToken(
    @RequestParam("client_id") String clientId,
    @RequestParam("redirect_uri") String redirectUri,
    @RequestParam("client_secret") String clientSecret,
    @RequestParam("code") String code
  );
}
