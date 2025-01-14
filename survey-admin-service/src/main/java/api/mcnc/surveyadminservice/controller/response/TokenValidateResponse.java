package api.mcnc.surveyadminservice.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 토큰 검증 결과
 *
 * @author :유희준
 * @since :2024-12-16 오후 8:03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenValidateResponse(
  Boolean isValid,
  String adminId
) {
  public static TokenValidateResponse validResponse(String adminId) {
    return new TokenValidateResponse(true, adminId);
  }
  public static TokenValidateResponse invalidResponse() {
    return new TokenValidateResponse(false, null);
  }
}
