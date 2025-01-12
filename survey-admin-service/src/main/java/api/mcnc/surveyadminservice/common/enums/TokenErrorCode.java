package api.mcnc.surveyadminservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 관련 실패 코드
 *
 * @author 유희준
 * @since :2024-11-26 오후 1:23
 */
@Getter
@AllArgsConstructor
public enum TokenErrorCode implements Code{


  TOKEN_EXPIRED("T000", "만료된 토큰 입니다."),
  INVALID_TOKEN("T001", "유효하지 않은 토큰입니다."),

  ;

  private final String code;
  private final String message;
}
