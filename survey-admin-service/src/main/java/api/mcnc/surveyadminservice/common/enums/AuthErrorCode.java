package api.mcnc.surveyadminservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 1:23
 */
@Getter
@AllArgsConstructor
public enum AuthErrorCode implements Code{


  ILLEGAL_REGISTRATION_ID("AU000", "지원하지 않는 접근입니다."),
  NO_ACCESS("AU001", "접근 권한이 존재하지 않습니다.")


  ;

  private final String code;
  private final String message;
}
