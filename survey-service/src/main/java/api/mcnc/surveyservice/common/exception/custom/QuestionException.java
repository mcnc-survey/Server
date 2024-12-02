package api.mcnc.surveyservice.common.exception.custom;

import api.mcnc.surveyservice.common.enums.Code;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-14 오후 9:40
 */
@Getter
public class QuestionException extends RuntimeException {
  private final Code code;

  public QuestionException(Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public QuestionException(Code code, String message) {
    super(message);
    this.code = code;
  }

}
