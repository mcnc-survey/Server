package api.mcnc.surveyrespondentservice.client.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-21 오후 10:27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthToken(
  @JsonProperty("access_token")
  String accessToken
) {
}
