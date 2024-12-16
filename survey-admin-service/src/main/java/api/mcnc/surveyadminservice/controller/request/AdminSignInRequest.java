package api.mcnc.surveyadminservice.controller.request;

import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import api.mcnc.surveyadminservice.common.annotation.PasswordEncryption;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-10 오후 10:59
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminSignInRequest {
  @Email @Setter
  @EmailEncryption
  private String email;
  @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "비밀번호 형식이 일치하지 않습니다.")
  private String password;
}
