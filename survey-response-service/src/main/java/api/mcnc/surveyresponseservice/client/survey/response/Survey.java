package api.mcnc.surveyresponseservice.client.survey.response;

import api.mcnc.surveyresponseservice.controller.response.SurveySnippet;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(
  use = JsonTypeInfo.Id.CLASS,
  include = JsonTypeInfo.As.WRAPPER_OBJECT
)
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

  public String startDateTime() {
    return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  public String endDateTime() {
    return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  public SurveySnippet toSnippet() {
    return SurveySnippet.builder()
      .id(id)
      .title(title)
      .description(description)
      .question(question)
      .startDateTime(startDateTime())
      .endDateTime(endDateTime())
      .build();
  }

}
