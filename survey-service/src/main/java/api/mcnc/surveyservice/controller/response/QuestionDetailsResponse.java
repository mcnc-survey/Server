package api.mcnc.surveyservice.controller.response;

import api.mcnc.surveyservice.entity.question.QuestionType;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:47
 */
@Builder
public record QuestionDetailsResponse(
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
