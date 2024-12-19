package api.mcnc.surveyresponseservice.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오후 5:19
 */
public record QuestionResponseUpdate(
  @NotBlank
  String id,
  @NotNull
  Boolean isRequired,
  String response
) {
}
