package api.mcnc.surveyrespondentservice.client.oauth.kakao;

import api.mcnc.surveyrespondentservice.client.oauth.OAuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 카카오 oauth 요청
 *
 * @author :유희준
 * @since :2024-11-21 오후 8:49
 */
@FeignClient(
  name = "kakaoFeignClient",
  url = "${kakao.token-uri}?grant_type=authorization_code"
)
public interface KakaoOAuthClient {

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  OAuthToken requestGetToken(
    @RequestParam("client_id") String clientId,
    @RequestParam("redirect_uri") String redirectUri,
    @RequestParam("client_secret") String clientSecret,
    @RequestParam("code") String code
  );

}