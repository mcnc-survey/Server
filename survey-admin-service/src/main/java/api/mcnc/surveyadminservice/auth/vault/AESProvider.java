package api.mcnc.surveyadminservice.auth.vault;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 암호화 모듈
 *
 * @author :유희준
 * @since :2024-12-08 오후 8:02
 */
public class AESProvider {
  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding"; // ECB mode, PKCS5Padding

  private final SecretKeySpec secretKey;

  public AESProvider(String key) {
    secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
  }

  /**
   * AES 암호화
   * @param plainText 암호화 할 문자열
   * @return 암호화된 문자열
   * @throws Exception
   */
  public String encrypt(String plainText) throws Exception {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * AES 복호화
   * @param encryptedText 복호화 할 문자열
   * @return 복호화된 문자열
   * @throws Exception
   */
  public String decrypt(String encryptedText) throws Exception {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
    byte[] decryptedBytes = cipher.doFinal(decodedBytes);
    return new String(decryptedBytes);
  }

  /**
   * 랜덤 문자열 생성
   * @param length 문자열 길이
   * @return 랜덤 문자열
   */
  public static String generateRandomString(int length) {
    byte[] randomBytes = new byte[length];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getEncoder().encodeToString(randomBytes);
  }
}
