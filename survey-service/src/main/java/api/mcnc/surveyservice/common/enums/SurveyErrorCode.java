package api.mcnc.surveyservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-14 오후 9:41
 */
@Getter
@AllArgsConstructor
public enum SurveyErrorCode implements Code{
  INTERNAL_SERVER_ERROR("S000", "예상 하지 못한 오류가 발생 했습니다."),
  FOUND_NOT_SURVEY("S001", "설문을 찾을 수 없습니다."),
  INVALID_REQUEST("S002", "잘못된 요청입니다.")

  ;


  private final String code;
  private final String message;
}
