package api.mcnc.surveyadminservice.common.aspect;

import api.mcnc.surveyadminservice.auth.vault.Vault;
import api.mcnc.surveyadminservice.common.annotation.EmailDecryption;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * AES 복호화 하는 AOP
 *
 * @author :Uheejoon
 * @since :2024-12-16 오전 12:54
 */
@Aspect
@Component
@RequiredArgsConstructor
public class EmailAESDecryptAspect {
  private final Vault vaultEncrypt;

  /**
   * 회원가입 이메일 복호화
   * @param result : 리턴 객체
   */
  @AfterReturning(pointcut = "execution(* api.mcnc.surveyadminservice.controller.AuthController.*(..))", returning = "result")
  public void beforeSignup(Object result) {
    // 반환 객체가 Api로 래핑된 경우에만 처리
    try {
      // 리플렉션을 사용하여 body 접근
      Object body = MethodUtils.invokeMethod(result, "body");
      if (body != null) {
        emailDecrypt(body);
      }
    } catch (Exception e) {
      // 예외 처리 로직 추가
      throw new RuntimeException("Email decryption failed", e);
    }
  }

  /**
   * 이메일 복호화
   * @param arg : 이메일 복호화 대상
   */
  private void emailDecrypt(Object arg) {
    if (ObjectUtils.isEmpty(arg)) return;
    FieldUtils.getAllFieldsList(arg.getClass())
      .stream()
      .filter(field -> field.isAnnotationPresent(EmailDecryption.class))
      .forEach(field -> {
        try {
          Object encryptionField = FieldUtils.readField(field, arg, true);
          if (!(encryptionField instanceof String)) return;
          String decrypted = vaultEncrypt.decrypt((String) encryptionField);
          FieldUtils.writeField(field, arg, decrypted, true);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      });
  }
}
