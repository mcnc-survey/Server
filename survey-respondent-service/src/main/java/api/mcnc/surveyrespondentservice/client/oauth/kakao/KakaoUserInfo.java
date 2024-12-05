package api.mcnc.surveyrespondentservice.client.oauth.kakao;

import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 12:47
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record
KakaoUserInfo(
  @JsonProperty("kakao_account")
  KakaoAccount kakaoAcount
) implements UserInfo{

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record KakaoAccount(
    @JsonProperty("name")
    String name,
    @JsonProperty("email")
    String email,
    @JsonProperty("phone_number")
    String phoneNumber
  ) implements UserInfo { }

  @Override
  public String email() {
    return kakaoAcount.email();
  }

  @Override
  public String name() {
    return kakaoAcount.name();
  }

  @Override
  public String phoneNumber() {
    return kakaoAcount.phoneNumber();
  }
}
