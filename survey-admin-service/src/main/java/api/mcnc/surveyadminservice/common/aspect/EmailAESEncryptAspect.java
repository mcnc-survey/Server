package api.mcnc.surveyadminservice.common.aspect;

import api.mcnc.surveyadminservice.auth.vault.Vault;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-08 오후 8:32
 */
@Aspect
@Component
@RequiredArgsConstructor
public class EmailAESEncryptAspect {

  private final Vault vaultEncrypt;

  @Before("execution(* api.mcnc.surveyadminservice.controller.AuthController.*(..))")
  public void beforeSignup(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Arrays.stream(args)
      .forEach(this::emailEncrypt);
  }

  private void emailEncrypt(Object arg) {
    if (ObjectUtils.isEmpty(arg)) return;
    FieldUtils.getAllFieldsList(arg.getClass())
      .stream()
      .filter(field -> field.isAnnotationPresent(api.mcnc.surveyadminservice.common.annotation.EmailEncryption.class))
      .forEach(field -> {
        try {
          Object encryptionField = FieldUtils.readField(field, arg, true);
          if (!(encryptionField instanceof String)) return;
          String encrypted = vaultEncrypt.encrypt((String) encryptionField);
          FieldUtils.writeField(field, arg, encrypted, true);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      });
  }

}
