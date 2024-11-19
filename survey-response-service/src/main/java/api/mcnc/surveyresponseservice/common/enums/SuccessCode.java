package api.mcnc.surveyresponseservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 9:30
 */
@Getter
@AllArgsConstructor
public enum SuccessCode implements Code{

  SUCCESS("200","OK"),
  RESPONSE_CREATE_SUCCESS("201","CREATED"),
  RESPONSE_UPDATE_SUCCESS("200", "UPDATED"),


  ;

  private final String code;
  private final String message;
}