package api.mcnc.surveyresponseservice.controller.request;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오후 1:13
 */
public record QuestionResponse(
  @NotBlank
  String questionId,
  @NotNull
  QuestionType questionType,
  @NotNull @Min(1)
  Integer orderNumber,
  @NotNull
  Boolean isRequired,
  @NotBlank
  String response
) {
}
