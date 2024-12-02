package api.mcnc.surveyrespondentservice.client.oauth.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 10:07
 */
@FeignClient(
  name = "googleUserClient",
  url = "${google.user-info-uri}"
)
public interface GoogleUserClient {
  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  GoogleUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);
}
