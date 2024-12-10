package api.mcnc.surveyresponseservice.controller.response.aggregation;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-09 오후 4:54
 */
@Builder
public record ResponseSnippet(
  String text,
  Integer count
) {
  public static ResponseSnippet of(String text, Integer count) {
    return ResponseSnippet.builder()
      .text(text)
      .count(count)
      .build();
  }

}
