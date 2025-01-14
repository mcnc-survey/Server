package api.mcnc.surveyadminservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Vault 관련 실패 코드
 *
 * @author 유희준
 * @since :2024-12-08 오후 8:08
 */
@Getter
@AllArgsConstructor
public enum VaultErrorCode implements Code {
  VAULT_ERROR("V000", "Vault Error"),
  VAULT_NOT_FOUND("V001", "Vault Not Found"),
  ;

  private final String code;
  private final String message;
}
