package api.mcnc.surveyservice.client.response;

import api.mcnc.surveyservice.entity.question.QuestionType;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2025-01-03 오전 11:23
 */
@Builder(access = lombok.AccessLevel.PRIVATE)
public record ResponseUpdate(
  String surveyId,
  String questionId,
  Integer orderNumber,
  QuestionType questionType
) {
  public static ResponseUpdate of(String surveyId, String questionId, Integer orderNumber, QuestionType questionType) {
    return ResponseUpdate.builder()
      .surveyId(surveyId)
      .questionId(questionId)
      .orderNumber(orderNumber)
      .questionType(questionType)
      .build();
  }
}
