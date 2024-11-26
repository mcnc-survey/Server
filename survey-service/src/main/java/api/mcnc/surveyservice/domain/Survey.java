package api.mcnc.surveyservice.domain;

import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.QuestionDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyCalendarResponse;
import api.mcnc.surveyservice.controller.response.SurveyDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.entity.survey.SurveyLike;
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
  LocalDateTime endAt,
  SurveyLike like,
  LocalDateTime modifiedAt
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
      .status(this.status)
      .lastModifiedAt(this.modifiedAt.toString())
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


}
