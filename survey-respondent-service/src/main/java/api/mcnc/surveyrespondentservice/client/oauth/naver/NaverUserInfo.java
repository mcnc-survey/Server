package api.mcnc.surveyrespondentservice.client.oauth.naver;

import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 5:05
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverUserInfo(NaverResponse response) implements UserInfo {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record NaverResponse(
    @JsonProperty("name")
    String name,
    @JsonProperty("email")
    String email,
    @JsonProperty("mobile")
    String phoneNumber
  )
  {}

  @Override
  public String email() {
    return response.email();
  }

  @Override
  public String name() {
    return response.email();
  }

  @Override
  public String phoneNumber() {
    return response.phoneNumber();
  }
}
