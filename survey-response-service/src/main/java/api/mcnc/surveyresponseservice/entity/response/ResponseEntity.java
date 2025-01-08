package api.mcnc.surveyresponseservice.entity.response;

import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyresponseservice.service.request.UpdateCommand;
import api.mcnc.surveyresponseservice.service.request.UpdateSurveyCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 10:00
 */
@Entity
@Table(name = "responses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseEntity extends MutableBaseEntity {
  @Id
  @Column(name = "ID")
  @Getter
  private String id;
  @Column(name = "SURVEY_ID")
  private String surveyId;
  @Column(name = "RESPONDENT_ID")
  private String respondentId;
  @Getter
  @Column(name = "QUESTION_ID")
  private String questionId;
  @Enumerated(EnumType.STRING)
  @Column(name = "QUESTION_TYPE")
  private QuestionType questionType;
  @Column(name = "ORDER_NUMBER")
  private Integer orderNumber;
  @Column(name = "RESPONSE")
  private String response;
  @Column(name = "ETC")
  private String etc;

  public Integer orderNumber() {
    return this.orderNumber;
  }

  public boolean isHasTextResponse() {
    return StringUtils.hasText(this.response) || StringUtils.hasText(this.etc);
  }

  @Builder
  private ResponseEntity(String id, String surveyId, String respondentId, String questionId, QuestionType questionType, Integer orderNumber, String response, String etc) {
    this.id = id;
    this.surveyId = surveyId;
    this.respondentId = respondentId;
    this.questionId = questionId;
    this.questionType = questionType;
    this.orderNumber = orderNumber;
    this.response = response;
    this.etc = etc;
  }

  public static ResponseEntity of(Response response) {
    return ResponseEntity.builder()
      .id(UUID.randomUUID().toString())
      .surveyId(response.surveyId())
      .questionId(response.questionId())
      .questionType(response.questionType())
      .orderNumber(response.orderNumber())
      .respondentId(response.respondentId())
      .response(response.response())
      .etc(response.etc())
      .build();
  }

  public void update(UpdateCommand updateCommand) {
    this.response = updateCommand.response();
    this.etc = updateCommand.etc();
  }

  public Response toDomain() {
    return Response.builder()
      .id(this.id)
      .surveyId(this.surveyId)
      .respondentId(this.respondentId)
      .questionId(this.questionId)
      .questionType(this.questionType)
      .orderNumber(this.orderNumber)
      .response(this.response)
      .etc(this.etc)
      .build();
  }

  public void update(UpdateSurveyCommand updateSurveyCommand) {
    this.orderNumber = updateSurveyCommand.orderNumber();
    this.questionType = updateSurveyCommand.type();
  }
}
