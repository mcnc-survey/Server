package api.mcnc.surveyadminservice.controller.request;

import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import api.mcnc.surveyadminservice.common.annotation.PasswordEncryption;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.AccessLevel;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-09 오전 9:44
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminSignUpRequest {
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Pattern(regexp = "^(\\d{2,3})(\\d{3,4})(\\d{4})$", message = "전화번호 형식이 올바르지 않습니다.")
  private String phoneNumber;
  @Email @Setter
  @EmailEncryption
  private String email;
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
  @PasswordEncryption
  private String password;

  public String getFullName() {
    return firstName + lastName;
  }
}
