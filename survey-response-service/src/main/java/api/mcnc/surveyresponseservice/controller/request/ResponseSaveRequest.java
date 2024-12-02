package api.mcnc.surveyresponseservice.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오후 1:06
 */
public record ResponseSaveRequest(
  @Valid
  List<QuestionResponse> responses
) {
}
