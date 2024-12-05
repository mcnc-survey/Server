package api.mcnc.surveyservice.controller.request;

import api.mcnc.surveyservice.entity.question.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-24 오후 4:37
 */
public record SurveyUpdateRequest(
  @NotBlank
  String title,
  String description,
  @NotNull
  LocalDateTime startAt,
  @NotNull
  LocalDateTime endAt,
  @Valid
  List<Question> updateQuestionList

) {
    public record Question(
      String id,
      @NotBlank
      String title,
      @NotNull
      QuestionType questionType,
      @Min(1)
      Integer order,
      String columns,
      String rows,
      @NotNull
      Boolean required,
      Boolean etc
    ) { }


}
