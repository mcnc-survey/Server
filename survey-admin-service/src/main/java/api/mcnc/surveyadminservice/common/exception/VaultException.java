package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.Code;

/**
 * Vault 관련 예외
 *
 * @author :유희준
 * @since :2024-12-08 오후 8:06
 */
public class VaultException extends AdminException{
  public VaultException(Code code) {
    super(code);
  }

  public VaultException(Code code, String description) {
    super(code, description);
  }
}
