package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.QuestionDetailsResponse;
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
  String title,
  QuestionType questionType,
  Integer order,
  String columns,
  String rows,
  Boolean required,
  Boolean etc
) {
  public static Question fromRequest(QuestionCreateRequest request) {
    return Question.builder()
      .title(request.title())
      .questionType(request.questionType())
      .order(request.order())
      .columns(request.columns())
      .rows(request.rows())
      .required(request.required())
      .etc(request.etc())
      .build();
  }
  public QuestionDetailsResponse toDetailsResponse() {
    return QuestionDetailsResponse.builder()
      .id(this.id)
      .title(this.title)
      .questionType(this.questionType)
      .order(this.order)
      .columns(this.columns)
      .rows(this.rows)
      .required(this.required)
      .etc(this.etc)
      .build();
  }

  public static Question fromRequest(SurveyUpdateRequest.Question request) {
    return Question.builder()
      .id(request.id())
      .title(request.title())
      .questionType(request.questionType())
      .order(request.order())
      .columns(request.columns())
      .rows(request.rows())
      .required(request.required())
      .etc(request.etc())
      .build();
  }

}
