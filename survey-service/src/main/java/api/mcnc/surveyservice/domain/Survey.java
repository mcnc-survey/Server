package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 9:52
 */
@Builder
public record Survey(
  String id,
  String adminId,
  String title,
  String description,
  List<Question> question,
  SurveyStatus status,
  LocalDateTime startAt,
  LocalDateTime endAt
) {
  public static Survey fromRequest(String adminId, String title, String description, LocalDateTime startAt, LocalDateTime endAt) {
    return Survey.builder()
      .adminId(adminId)
      .title(title)
      .description(description)
      .status(SurveyStatus.WAIT)
      .startAt(startAt)
      .endAt(endAt)
      .build();
  }

  public SurveyResponse toResponse() {
    return SurveyResponse.builder()
      .id(this.id)
      .title(this.title)
      .description(this.description)
      .status(this.status)
      .startAt(this.startAt.toString())
      .endAt(this.endAt.toString())
      .build();
  }

}
