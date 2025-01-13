package api.mcnc.surveyadminservice.domain;

import lombok.Builder;

// TODO 2024-12-29 yhj : 로직 위치를 변경 해야함 수정 필요
/**
 * 히스토리
 *
 * @author :유희준
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
