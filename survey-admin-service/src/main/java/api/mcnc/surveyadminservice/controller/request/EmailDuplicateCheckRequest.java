package api.mcnc.surveyadminservice.controller.request;

import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-15 오후 9:41
 */
public class EmailDuplicateCheckRequest{
  @Getter @Setter
  @Email @EmailEncryption
  private String email;
}
