package api.mcnc.surveyresponseservice.client.survey.response;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:01
 */
@Builder
public record Question(
  String id,
  String title,
  QuestionType questionType,
  Integer order,
  String columns,
  String rows,
  Boolean required,
  Boolean etc
) {
}
