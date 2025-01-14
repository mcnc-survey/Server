package api.mcnc.surveyadminservice.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 토큰 응답
 *
 * @author :유희준
 * @since :2024-12-15 오후 11:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
  String userName,
  String accessToken
) {
  public static TokenResponse of(String userName, String accessToken) {
    return new TokenResponse(userName, accessToken);
  }

  public static TokenResponse of(String reIssueAccessToken) {
    return new TokenResponse(null, reIssueAccessToken);
  }
}
