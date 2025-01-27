package api.mcnc.surveyadminservice.controller.request;

import api.mcnc.surveyadminservice.common.annotation.PasswordEncryption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 변경 요청 객체
 *
 * @author :유희준
 * @since :2024-12-17 오후 4:43
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
  @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "비밀번호 형식이 일치하지 않습니다.")
  @PasswordEncryption
  private String newPassword;
  @NotBlank
  private String token;
}
