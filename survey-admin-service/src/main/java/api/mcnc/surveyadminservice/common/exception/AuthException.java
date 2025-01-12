package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.Code;

/**
 * 인증 관련 예외
 *
 * @author 유희준
 * @since :2024-11-26 오후 1:33
 */
public class AuthException extends AdminException{
  public AuthException(Code code) {
    super(code);
  }

  public AuthException(Code code, String description) {
    super(code, description);
  }
}
