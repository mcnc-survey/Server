package api.mcnc.surveyresponseservice.controller.response.aggregation;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-09 오후 4:37
 */
@Builder
public record SurveySummary(
  Integer respondentCount,
  String endDate,
  String lastModifiedDate
) {
  public static SurveySummary of(Integer respondentCount, String endDate, String lastModifiedDate) {
    return SurveySummary.builder()
      .respondentCount(respondentCount)
      .endDate(endDate)
      .lastModifiedDate(lastModifiedDate)
      .build();
  }
}
