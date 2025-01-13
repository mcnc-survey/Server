package api.mcnc.surveyresponseservice.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * 응답 수정
 *
 * @author :유희준
 * @since :2024-11-15 오후 5:09
 */
public record ResponseUpdateRequest(
  @Valid
  List<QuestionResponseUpdate> responses
) {
}
