package api.mcnc.surveyresponseservice.controller.request;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

/**
 * 응답 수정
 * @author :유희준
 */
@Builder(access = lombok.AccessLevel.PRIVATE)
public record ResponseUpdate(
  String surveyId,
  String questionId,
  Integer orderNumber,
  QuestionType questionType
) {}