package api.mcnc.surveyresponseservice.controller.request;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 응답 수정
 *
 * @author :유희준
 * @since :2024-11-15 오후 5:19
 */
public record QuestionResponseUpdate(
  @NotBlank
  String id,
  @NotNull
  QuestionType questionType,
  @NotNull
  Boolean isRequired,
  String response,
  String etc
) {
}
