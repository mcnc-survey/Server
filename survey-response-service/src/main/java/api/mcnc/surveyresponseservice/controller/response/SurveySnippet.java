package api.mcnc.surveyresponseservice.controller.response;

import api.mcnc.surveyresponseservice.client.survey.response.Question;
import lombok.Builder;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-11 오후 2:05
 */
@Builder
public record SurveySnippet(
  String id,
  String title,
  String description,
  List<Question> question,
  String startDateTime,
  String endDateTime
) {
}
