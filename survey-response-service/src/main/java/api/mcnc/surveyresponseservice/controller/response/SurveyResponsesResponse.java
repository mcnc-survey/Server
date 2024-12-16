package api.mcnc.surveyresponseservice.controller.response;

import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-11 오후 2:01
 */
public record SurveyResponsesResponse(
  SurveySnippet surveySnippet,
  Map<Integer, ResponseResult> responseResult
) {
  public static SurveyResponsesResponse of(SurveySnippet snippet, Map<Integer, ResponseResult> result) {
    return new SurveyResponsesResponse(snippet, result);
  }
}
