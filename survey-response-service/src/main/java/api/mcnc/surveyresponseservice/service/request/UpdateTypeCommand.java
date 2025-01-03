package api.mcnc.surveyresponseservice.service.request;

import api.mcnc.surveyresponseservice.controller.request.ResponseUpdate;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2025-01-03 오전 11:35
 */
@Builder(access = lombok.AccessLevel.PRIVATE)
public record UpdateTypeCommand(
  String questionId,
  QuestionType type
) {
  public static UpdateTypeCommand of(ResponseUpdate responseUpdate) {
    return UpdateTypeCommand.builder()
      .questionId(responseUpdate.questionId())
      .type(responseUpdate.questionType())
      .build();
  }
}
