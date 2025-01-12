package api.mcnc.surveyservice.controller.response;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-26 오후 5:32
 */
@Builder
public record SurveyLikeResponse(
  String id,
  String title
) {
}
