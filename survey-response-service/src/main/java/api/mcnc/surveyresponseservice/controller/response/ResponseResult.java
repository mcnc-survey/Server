package api.mcnc.surveyresponseservice.controller.response;

import lombok.Builder;

/**
 * 응답 결과
 *
 * @author :유희준
 * @since :2024-11-15 오전 9:49
 */
@Builder
public record ResponseResult(
  String id,
  Object response,
  String etc
)
{ }
