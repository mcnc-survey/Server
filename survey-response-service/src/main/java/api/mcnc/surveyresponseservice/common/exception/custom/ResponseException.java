package api.mcnc.surveyresponseservice.common.exception.custom;

import api.mcnc.surveyresponseservice.common.enums.Code;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-14 오후 9:40
 */
@Getter
public class ResponseException extends RuntimeException {
  private final Code code;

  public ResponseException(Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public ResponseException(Code code, String message) {
    super(message);
    this.code = code;
  }

}
