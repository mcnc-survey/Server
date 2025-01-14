package api.mcnc.surveyrespondentservice.authentication.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * email 유저 정보
 *
 * @author :유희준
 * @since :2024-12-05 오후 4:40
 */
public record EmailUserInfo(
  @Email
  String email,
  @NotBlank
  String name,
  @NotBlank
  String phoneNumber
) implements UserInfo {
}
