package api.mcnc.surveyadminservice.common.result;

import api.mcnc.surveyadminservice.common.enums.Code;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 공통 응답 객체
 *
 * @author 유희준
 * @since :2024-11-15 오전 9:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Api<T>(
  Boolean success,
  String resultCode,
  String message,
  T body
) {

  /**
   * 성공 시
   * @param code Code 정보
   * @param body 데이터
   * @return {@link Api}
   */
  public static <T> Api<T> ok(Code code, T body) {
    return new Api<>(true, code.getCode(), code.getMessage(), body);
  }

  /**
   * 실패 시
   * @param code Code 정보
   * @param body 데이터
   * @return {@link Api}
   */
  public static <T> Api<T> fail(Code code, T body) {
    return new Api<>(false, code.getCode(), code.getMessage(), body);
  }
  
}
