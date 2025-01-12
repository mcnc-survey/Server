package api.mcnc.surveyadminservice.auth.vault;

import api.mcnc.surveyadminservice.common.enums.VaultErrorCode;
import api.mcnc.surveyadminservice.common.exception.VaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;

/**
 * Vault 서버에 저장된 DB Key를 이용하여 암호화 및 복호화를 수행하는 클래스
 * @author :유희준
 * @since :2024-12-08 오후 7:30
 */
@Component
public class Vault {
  private final AESProvider encryptor;
  private final String KEY_PATH = "kv-v1/data/encrypt";
  private final String DB_KEY = "dbkey";
  private final String KEY_NAME = "key";

  public Vault(VaultTemplate vaultTemplate) {
    VaultKeyValueOperations ops = vaultTemplate.opsForKeyValue(KEY_PATH, VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
    String key;
    try {
      Map<String, Object> dbkey = ops.get(DB_KEY).getRequiredData();
      key = (String) dbkey.get(KEY_NAME);
    } catch(NullPointerException | IllegalStateException e) {
      throw new VaultException(VaultErrorCode.VAULT_NOT_FOUND);
    }
    this.encryptor = new AESProvider(key);
  }

  /**
   * 암호화
   * @param plainText : 암호화할 문자열
   * @return : 암호화된 문자열
   */
  public String encrypt(String plainText)  {
    try {
      return encryptor.encrypt(plainText);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 복호화
   * @param encryptedText : 복호화할 문자열
   * @return : 복호화된 문자열
   */
  public String decrypt(String encryptedText) {
    try {
      return encryptor.decrypt(encryptedText);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
