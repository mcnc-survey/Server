package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.response.*;
import api.mcnc.surveyservice.entity.survey.SurveyLike;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import lombok.*;
import org.apache.hc.client5.http.utils.Base64;

import java.time.LocalDateTime;
import java.util.List;

import static api.mcnc.surveyservice.common.constants.SurveyLink.SURVEY_LINK;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 9:52
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

  public static Survey fromRequest(String adminId, String title, String description, SurveyStatus status, LocalDateTime startAt, LocalDateTime endAt) {
    return Survey.builder()
      .adminId(adminId)
      .title(title)
      .description(description)
      .status(status)
      .startAt(startAt)
      .endAt(endAt)
      .build();
  }

  public String surveyLink() {
    return SURVEY_LINK + Base64.encodeBase64String(this.id.getBytes());
  }

  public SurveyResponse toResponse() {
    return SurveyResponse.builder()
      .id(this.id)
      .title(this.title)
      .status(this.status)
      .lastModifiedAt(this.modifiedAt.toString())
      .startAt(this.startAt.toString())
      .endAt(this.endAt.toString())
      .surveyLink(this.surveyLink())
      .isLike(SurveyLike.LIKE.equals(this.like))
      .build();
  }

  public SurveyDetailsResponse toDetailsResponse() {
    List<QuestionDetailsResponse> questionDeatilsList = this.question.stream().map(Question::toDetailsResponse).toList();
    return SurveyDetailsResponse.builder()
      .id(this.id)
      .title(this.title)
      .description(this.description)
      .question(questionDeatilsList)
      .startAt(this.startAt.toString())
      .endAt(this.endAt.toString())
      .build();
  }

  public SurveyCalendarResponse toCalendarResponse() {
    return SurveyCalendarResponse.builder()
      .id(this.id)
      .title(this.title)
      .startAt(this.startAt.toString())
      .endAt(this.endAt.toString())
      .build();
  }

  public SurveyLikeResponse toLikeResponse() {
    return SurveyLikeResponse.builder()
      .id(this.id)
      .title(this.title)
      .build();
  }

}
