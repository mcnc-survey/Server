package api.mcnc.surveyresponseservice.controller.response.aggregation;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

import java.util.List;

/**
 * 설문 응답 정보
 *
 * @author :유희준
 * @since :2024-12-09 오후 4:37
 */
@Builder
public record SurveyResultValue(
  String questionId,
  String questionTitle,
  QuestionType questionType,
  Integer totalResponseCount,
  Integer responseCount,
  List<Object> responses,
  Integer etcCount,
  List<String> etc
) {
}
