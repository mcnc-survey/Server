package api.mcnc.surveyresponseservice.client.survey.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:01
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
  LocalDateTime endAt,
  SurveyLike like,
  LocalDateTime modifiedAt
) {
  public String lastModifiedDate() {
    return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
