package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.Code;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오전 10:27
 */
@Getter
public class AdminException extends RuntimeException{
  private final Code code;

  public AdminException(Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public AdminException(Code code, String desciption) {
    super(desciption);
    this.code = code;
  }
}
