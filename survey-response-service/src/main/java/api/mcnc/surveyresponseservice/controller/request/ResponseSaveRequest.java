package api.mcnc.surveyresponseservice.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * 처음 응답
 *
 * @author :유희준
 * @since :2024-11-15 오후 1:06
 */
public record ResponseSaveRequest(
  @Valid
  List<QuestionResponse> responses
) {
}
