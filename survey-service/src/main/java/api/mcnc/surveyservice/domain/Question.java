package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.QuestionDetailsResponse;
import api.mcnc.surveyservice.entity.question.QuestionType;
import lombok.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 1:42
 */
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question{
  private String id;
  private String title;
  private QuestionType questionType;
  private Integer order;
  private String columns;
  private String rows;
  private Boolean required;
  private Boolean etc;

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
    // TODO 2024-12-11 yhj : columns |`|로 쪼개서 줘야할듯
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
