package api.mcnc.surveyresponseservice.controller.request;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

@Builder(access = lombok.AccessLevel.PRIVATE)
public record ResponseUpdate(
  String surveyId,
  String questionId,
  QuestionType questionType
) {}