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
public record UpdateSurveyCommand(
  String questionId,
  Integer orderNumber,
  QuestionType type
) {
  public static UpdateSurveyCommand of(ResponseUpdate responseUpdate) {
    return UpdateSurveyCommand.builder()
      .questionId(responseUpdate.questionId())
      .orderNumber(responseUpdate.orderNumber())
      .type(responseUpdate.questionType())
      .build();
  }
}
