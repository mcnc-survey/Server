package api.mcnc.surveyservice.controller.request;

import api.mcnc.surveyservice.entity.question.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-19 오후 11:19
 */
public record QuestionCreateRequest(
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
  @NotNull
  Boolean etc
) { }
