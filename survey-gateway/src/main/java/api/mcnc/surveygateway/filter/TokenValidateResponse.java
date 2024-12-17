package api.mcnc.surveygateway.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오후 8:03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenValidateResponse(
  Boolean isValid,
  String adminId
) {
}
