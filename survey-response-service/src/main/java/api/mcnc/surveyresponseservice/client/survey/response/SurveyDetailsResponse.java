package api.mcnc.surveyresponseservice.client.survey.response;

import lombok.Builder;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:01
 */
@Builder
public record SurveyDetailsResponse(
  String id,
  String title,
  String description,
  String startAt,
  String endAt,
  String lastModifiedDate,
  List<QuestionDetailsResponse> question
) {
}
