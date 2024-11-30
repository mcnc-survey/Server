package api.mcnc.surveyservice.controller.response;

import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 4:29
 */
@Builder
public record SurveyResponse(
  String id,
  String title,
  SurveyStatus status,
  String lastModifiedAt,
  String surveyLink
) {
}
