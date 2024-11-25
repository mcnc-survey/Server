package api.mcnc.surveyadminservice.domain;

import api.mcnc.surveyadminservice.entity.admin.AdminRole;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 8:02
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
