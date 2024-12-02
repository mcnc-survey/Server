package api.mcnc.surveyresponseservice.service.request;

import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오후 5:48
 */
@Builder
public record UpdateCommand(
  String id,
  String response
) {
  public static UpdateCommand of(QuestionResponseUpdate updateRequest) {
    return UpdateCommand.builder()
      .id(updateRequest.id())
      .response(updateRequest.response())
      .build();
  }
}
