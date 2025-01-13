package api.mcnc.surveyadminservice.controller.response;

/**
 * 이메일 검증 결과
 *
 * @author :유희준
 * @since :2024-12-19 오전 11:27
 */
public record EmailVerifyCheckResponse(
  Boolean isValid
) {
}
