package api.mcnc.surveyresponseservice.controller.response.aggregation;

import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import lombok.Builder;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
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
