package api.mcnc.surveyadminservice.controller.request;

import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 중복 여부 요청 객체
 *
 * @author :유희준
 * @since :2024-12-15 오후 9:41
 */
public class EmailDuplicateCheckRequest{
  @Getter @Setter
  @Email @EmailEncryption
  private String email;
}
