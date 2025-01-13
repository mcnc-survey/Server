package api.mcnc.surveyresponseservice.client.survey.response;

import api.mcnc.surveyresponseservice.controller.response.SurveySnippet;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 설문
 *
 * @author :유희준
 * @since :2024-11-27 오후 2:01
 */
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey{
  private String id;
  private String adminId;
  private String title;
  private String description;
  private List<Question> question;
  private SurveyStatus status;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private SurveyLike like;
  private LocalDateTime modifiedAt;

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
