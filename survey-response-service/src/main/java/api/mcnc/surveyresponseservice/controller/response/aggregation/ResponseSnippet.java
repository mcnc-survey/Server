package api.mcnc.surveyresponseservice.controller.response.aggregation;

import lombok.Builder;

/**
 * 설문 간략 정보
 *
 * @author :유희준
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
