package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.entity.question.QuestionType;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 1:42
 */
@Builder
public record Question(
  String id,
//  Survey survey, // 순환 참조 때문에 있어야 할 지 고민
  String title,
  QuestionType questionType,
  Integer order,
  String columns,
  String rows
) {
  public static Question fromRequest(QuestionCreateRequest request) {
    return Question.builder()
      .title(request.title())
      .questionType(request.questionType())
      .order(request.order())
      .columns(request.columns())
      .rows(request.rows())
      .build();
  }

}
