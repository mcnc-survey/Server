package api.mcnc.surveyrespondentservice.common.exception.custom;

import api.mcnc.surveyrespondentservice.common.enums.Code;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 10:38
 */
public class TokenException extends RespondentException{
  public TokenException(Code code) {
    super(code);
  }

  public TokenException(Code code, String message) {
    super(code, message);
  }
}
