package api.mcnc.surveyadminservice.common.aspect;

import api.mcnc.surveyadminservice.auth.vault.Vault;
import api.mcnc.surveyadminservice.common.annotation.EmailEncryption;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Parameter;
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

  @Around("execution(* api.mcnc.surveyadminservice.controller.AuthController.*(..))")
  public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature)joinPoint.getSignature();
    Parameter[] parameters = signature.getMethod().getParameters();

    Object[] args = joinPoint.getArgs();

    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i].isAnnotationPresent(EmailEncryption.class)) {
        args[i] = vaultEncrypt.encrypt((String) args[i]);
      } else {
        emailEncrypt(args[i]);
      }
    }
    return joinPoint.proceed(args);
  }

  private void emailEncrypt(Object arg) {
    if (ObjectUtils.isEmpty(arg)) return;
    FieldUtils.getAllFieldsList(arg.getClass())
      .stream()
      .filter(field -> field.isAnnotationPresent(EmailEncryption.class))
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
