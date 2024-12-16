package api.mcnc.surveyadminservice.controller.response;

import api.mcnc.surveyadminservice.common.annotation.EmailDecryption;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-15 오후 9:43
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class EmailDuplicateCheckResponse {
  @EmailDecryption
  private String email;
  private Boolean isDuplicated;

  public static EmailDuplicateCheckResponse isDuplicated(String email, Boolean result) {
    return EmailDuplicateCheckResponse.builder()
      .email(email)
      .isDuplicated(result)
      .build();
  }
}
