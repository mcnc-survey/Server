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
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminSignUpRequest {
  @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$", message = "이름 형식이 올바르지 않습니다.")
  private String userName;
  @Pattern(regexp = "^(\\d{2,3})(\\d{3,4})(\\d{4})$", message = "전화번호 형식이 올바르지 않습니다.")
  private String phoneNumber;
  @Email @Setter
  @EmailEncryption
  private String email;
  @PasswordEncryption
  @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호 형식이 일치하지 않습니다.")
  private String password;
}
