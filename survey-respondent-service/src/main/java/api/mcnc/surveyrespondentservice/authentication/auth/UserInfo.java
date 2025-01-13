package api.mcnc.surveyrespondentservice.authentication.auth;

/**
 * social, email marked interface
 *
 * @author :유희준
 * @since :2024-11-22 오후 1:53
 */
public interface UserInfo {
  String email();
  String name();
  String phoneNumber();
}
