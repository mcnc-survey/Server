package api.mcnc.surveyresponseservice.controller.response.aggregation;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-09 오후 4:54
 */
@Builder
public record QuestionSnippet(
  String text,
  Integer count
) {
  public static QuestionSnippet of(String text, Integer count) {
    return QuestionSnippet.builder()
      .text(text)
      .count(count)
      .build();
  }

}
