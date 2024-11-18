package api.mcnc.surveyresponseservice.common.enums;

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
public enum ResponseErrorCode implements Code{
  INTERNAL_SERVER_ERROR("R000", "예상 하지 못한 오류가 발생 했습니다."),
  DUPLICATE_RESPONSE("R001", "이미 응답한 설문입니다."),
  NOT_FOUND_RESPONSE("R002", "응답을 찾을 수 없습니다."),
  NOT_FOUND_SURVEY("R003", "설문을 찾을 수 없습니다."),
  EMPTY_RESPONSE("R004", "빈 설문은 제출할 수 없습니다."),
  INVALID_REQUEST("R005", "잘못된 요청입니다."),



  ;
  private final String code;
  private final String message;
}
