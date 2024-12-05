package api.mcnc.surveyrespondentservice.client.oauth.google;

import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 10:23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserInfo(
  @JsonProperty("name")
  String name,
  @JsonProperty("email")
  String email
) implements UserInfo {
  @Override
  public String phoneNumber() {
    return "none";
  }
}
