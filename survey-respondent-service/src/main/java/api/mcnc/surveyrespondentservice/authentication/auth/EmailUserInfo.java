package api.mcnc.surveyrespondentservice.authentication.auth;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-05 오후 4:40
 */
public record EmailUserInfo(
  String email,
  String name,
  String phoneNumber
) implements UserInfo {
}
