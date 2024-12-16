package api.mcnc.surveyadminservice.common.enums;

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
public enum AdminErrorCode implements Code {
  INTERNAL_SERVER_ERROR("A000", "예상 하지 못한 오류가 발생 했습니다."),
  INVALID_REQUEST("A001", "잘못된 요청입니다."),
  NOT_FOUND("A002", "해당 관리자를 찾을 수 없습니다."),
  ALREADY_EXIST("A003", "이미 존재하는 관리자입니다."),
  MISS_MATCH_ADMIN_ACCOUNT("A004", "관리자 계정이 일치하지 않습니다.")

  ;




  private final String code;
  private final String message;
}
