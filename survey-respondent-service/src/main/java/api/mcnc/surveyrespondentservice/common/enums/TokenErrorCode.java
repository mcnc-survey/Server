package api.mcnc.surveyrespondentservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 10:39
 */
@Getter
@AllArgsConstructor
public enum TokenErrorCode implements Code {
  INVALID_TOKEN("T000", "올바르지 않은 토큰입니다."),
  INVALID_JWT_SIGNATURE("T001", "잘못된 JWT 시그니처입니다."),

  ;

  private final String code;
  private final String message;

}
