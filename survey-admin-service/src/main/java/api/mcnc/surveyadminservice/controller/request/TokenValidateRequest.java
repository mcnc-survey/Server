package api.mcnc.surveyadminservice.controller.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 검증 요청 객체
 *
 * @author :유희준
 * @since :2024-12-16 오후 8:01
 */
public record TokenValidateRequest(
  @NotBlank
  String accessToken
) {
}
