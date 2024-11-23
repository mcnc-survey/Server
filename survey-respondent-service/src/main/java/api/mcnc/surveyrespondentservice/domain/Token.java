package api.mcnc.surveyrespondentservice.domain;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 3:03
 */
public record Token(
  String accessToken
) {
  public static Token of(String accessToken) {
    return new Token(accessToken);
  }
}
