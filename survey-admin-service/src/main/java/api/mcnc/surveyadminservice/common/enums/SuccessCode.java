package api.mcnc.surveyadminservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 성공 관련 코드
 *
 * @author 유희준
 * @since :2024-11-15 오전 9:30
 */
@Getter
@AllArgsConstructor
public enum SuccessCode implements Code {

  SUCCESS("200","OK"),
  RESPONSE_CREATE_SUCCESS("201","CREATED"),
  RESPONSE_UPDATE_SUCCESS("200", "UPDATED"),


  ;

  private final String code;
  private final String message;
}
