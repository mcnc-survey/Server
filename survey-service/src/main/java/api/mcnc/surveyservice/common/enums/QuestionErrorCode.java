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
public enum QuestionErrorCode implements Code{
  INTERNAL_SERVER_ERROR("Q000", "예상 하지 못한 오류가 발생 했습니다."),




  ;
  private final String code;
  private final String message;
}
