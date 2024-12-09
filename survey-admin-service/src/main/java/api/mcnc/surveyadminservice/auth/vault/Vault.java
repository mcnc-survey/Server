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
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-08 오후 7:30
 */
@Component
public class Vault {
  private final AESProvider encryptor;

  @Autowired
  public Vault(VaultTemplate vaultTemplate) {
    VaultKeyValueOperations ops = vaultTemplate.opsForKeyValue("kv-v1/encrypt/data", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
    String key;
    try {
      Map<String, Object> dbkey = ops.get("dbkey").getRequiredData();
      key = (String) dbkey.get("key");
    } catch(NullPointerException | IllegalStateException e) {
      throw new VaultException(VaultErrorCode.VAULT_NOT_FOUND);
    }
    this.encryptor = new AESProvider(key);
  }

  public String encrypt(String plainText)  {
    try {
      return encryptor.encrypt(plainText);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String decrypt(String encryptedText) {
    try {
      return encryptor.decrypt(encryptedText);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
