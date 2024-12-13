package api.mcnc.surveyadminservice.common.result;

import api.mcnc.surveyadminservice.common.enums.Code;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 9:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Api<T>(
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
