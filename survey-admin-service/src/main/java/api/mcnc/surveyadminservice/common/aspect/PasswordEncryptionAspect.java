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
 * 비밀번호 암호화 AOP
 *
 * @author :유희준
 * @since :2024-10-16 오후 9:47
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PasswordEncryptionAspect {

  private final PasswordEncoder passwordEncoder;

  /**
   * Controller 전체에 적용 <br>
   * 입력으로 들어온 객체에 @PasswordEncryption 어노테이션이 있으면 BCrypt 암호화
   * @param pjp 입력 객체
   * @throws Throwable
   */
  @Around("execution(* api.mcnc.surveyadminservice.controller..*.*(..))")
  public Object passwordEncryptionAspect(ProceedingJoinPoint pjp) throws Throwable {
    Arrays.stream(pjp.getArgs())
      .forEach(this::fieldEncryption);

    return pjp.proceed();
  }

  /**
   * 입력으로 들어온 객체에 @PasswordEncryption 어노테이션이 있으면 BCrypt 암호화
   * @param obj 입력 객체
   */
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
