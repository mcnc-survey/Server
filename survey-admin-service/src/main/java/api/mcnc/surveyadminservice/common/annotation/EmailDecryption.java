package api.mcnc.surveyadminservice.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AES 복호화 어노테이션
 *
 * @author :유희준
 * @since :2024-12-08 오후 8:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailDecryption {
}
