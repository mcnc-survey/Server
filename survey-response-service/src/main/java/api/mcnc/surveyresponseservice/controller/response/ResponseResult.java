package api.mcnc.surveyresponseservice.controller.response;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 9:49
 */
@Builder
public record ResponseResult(
  String id,
  String response
)
{ }
