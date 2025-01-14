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
 * AES 암호화 AOP
 *
 * @author :유희준
 * @since :2024-12-08 오후 8:32
 */
@Aspect
@Component
@RequiredArgsConstructor
public class EmailAESEncryptAspect {

  private final Vault vaultEncrypt;

  /**
   * Controller 전체에 적용 <br>
   * 입력으로 들어온 객체에 @EmailEncryption 어노테이션이 있으면 AES 암호화
   *
   * @param joinPoint 입력 객체
   * @throws Throwable
   */
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

  /**
   * AES 암호화
   * @param arg 입력 객체
   */
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
