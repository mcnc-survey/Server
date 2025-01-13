package api.mcnc.surveyrespondentservice.common.exception.custom;

import api.mcnc.surveyrespondentservice.common.enums.Code;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-14 오후 9:40
 */
@Getter
public class RespondentException extends RuntimeException {
  private final Code code;

  public RespondentException(Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public RespondentException(Code code, String message) {
    super(message);
    this.code = code;
  }

}
