package api.mcnc.surveyadminservice.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 비밀번호 암호화 어노테이션
 *
 * @author :유희준
 * @since :2024-10-16 오후 9:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PasswordEncryption {
}
