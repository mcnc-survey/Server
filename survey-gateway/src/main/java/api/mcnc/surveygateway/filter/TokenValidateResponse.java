package api.mcnc.surveygateway.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 토큰 결과 값
 *
 * @author :유희준
 * @since :2024-12-16 오후 8:03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenValidateResponse(
  Boolean isValid,
  String adminId
) {
}
