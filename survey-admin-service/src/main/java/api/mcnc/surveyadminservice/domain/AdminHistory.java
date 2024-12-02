package api.mcnc.surveyadminservice.domain;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:18
 */
@Builder
public record AdminHistory(
  String id,
  String adminId,
  String adminRole,
  String clientIp,
  String reqMethod,
  String reqUrl,
  String reqHeader,
  String reqPayload
) {
}
