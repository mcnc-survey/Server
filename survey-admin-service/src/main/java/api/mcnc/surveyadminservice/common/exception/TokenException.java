package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.Code;

/**
 * 토큰 관련 예외
 *
 * @author 유희준
 * @since :2024-11-26 오후 12:52
 */
public class TokenException extends AdminException{
  public TokenException(Code code) {
    super(code);
  }

  public TokenException(Code code, String description) {
    super(code, description);
  }
}
