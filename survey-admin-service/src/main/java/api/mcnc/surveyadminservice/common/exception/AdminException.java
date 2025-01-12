package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.Code;
import lombok.Getter;

/**
 * Admin 관련 예외
 *
 * @author 유희준
 * @since :2024-11-26 오전 10:27
 */
@Getter
public class AdminException extends RuntimeException{
  protected final Code code;

  public AdminException(Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public AdminException(Code code, String description) {
    super(description);
    this.code = code;
  }
}
