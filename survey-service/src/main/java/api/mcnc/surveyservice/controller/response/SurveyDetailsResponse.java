package api.mcnc.surveyservice.controller.response;

import lombok.Builder;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-23 오후 8:37
 */
@Builder
public record SurveyDetailsResponse(
  String id,
  String title,
  String description,
  String startAt,
  String endAt,
  List<QuestionDetailsResponse> question
) {
}
