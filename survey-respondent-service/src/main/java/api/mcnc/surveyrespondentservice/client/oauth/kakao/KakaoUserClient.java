package api.mcnc.surveyrespondentservice.client.oauth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 9:54
 */
@FeignClient(
  name = "kakaoUserClient",
  url = "${kakao.user-info-uri}"
)
public interface KakaoUserClient {

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  KakaoUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);
}