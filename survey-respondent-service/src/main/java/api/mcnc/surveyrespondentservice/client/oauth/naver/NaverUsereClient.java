package api.mcnc.surveyrespondentservice.client.oauth.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 네이버 유저 정보 요청
 *
 * @author :유희준
 * @since :2024-11-21 오후 10:18
 */
@FeignClient(
  name = "naverUsereClient",
  url = "${naver.user-info-uri}"
)
public interface NaverUsereClient {
  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  NaverUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);
}
