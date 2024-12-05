package api.mcnc.surveyservice.common.enums;

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
  SURVEY_CREATE_SUCCESS("201","CREATED"),
  SURVEY_UPDATE_SUCCESS("200", "UPDATED"),
  SURVEY_DELETE_SUCCESS("204", "DELETED" ),
  SURVEY_RESTORE_SUCCESS("204", "RESTORED" ),

  ;



  private final String code;
  private final String message;
}
