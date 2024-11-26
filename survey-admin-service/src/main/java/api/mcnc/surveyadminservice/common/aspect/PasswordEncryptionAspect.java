package api.mcnc.surveyadminservice.common.aspect;

import api.mcnc.surveyadminservice.common.annotation.PasswordEncryption;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-10-16 오후 9:47
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PasswordEncryptionAspect {

  private final PasswordEncoder passwordEncoder;

  @Around("execution(* api.mcnc.surveyadminservice.controller..*.*(..))")
  public Object passwordEncryptionAspect(ProceedingJoinPoint pjp) throws Throwable {
    Arrays.stream(pjp.getArgs())
      .forEach(this::fieldEncryption);

    return pjp.proceed();
  }

  private void fieldEncryption(Object obj) {
    if(ObjectUtils.isEmpty(obj)) {
      return;
    }
    FieldUtils.getAllFieldsList(obj.getClass()).
      stream().
      filter(field -> field.isAnnotationPresent(PasswordEncryption.class)).
      forEach(field -> {
        try {
          Object encryptionField = FieldUtils.readField(field, obj, true);
          if(!(encryptionField instanceof String)) {
            return;
          }
          String encrypted = passwordEncoder.encode((String) encryptionField);
          FieldUtils.writeField(field, obj, encrypted, true);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
  }
}
