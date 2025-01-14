package api.mcnc.surveyrespondentservice.common.result;

import api.mcnc.surveyrespondentservice.common.enums.Code;
import com.fasterxml.jackson.annotation.JsonInclude;

// TODO 2024-12-05 yhj : common 다 공통 로직으로 분리 해야하는데
/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-15 오전 9:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Api <T>(
  Boolean success,
  String resultCode,
  String message,
  T body
) {
  
  // 성공
  public static <T> Api<T> ok(Code code, T body) {
    return new Api<>(true, code.getCode(), code.getMessage(), body);
  }

  // 실패
  public static <T> Api<T> fail(Code code, T body) {
    return new Api<>(false, code.getCode(), code.getMessage(), body);
  }
  
}
