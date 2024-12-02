package api.mcnc.surveyservice.controller.response;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 3:51
 */
@Builder
public record SurveyCalendarResponse(
  String id,
  String title,
  String startAt,
  String endAt
) {
}
