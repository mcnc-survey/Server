package api.mcnc.surveyadminservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
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
