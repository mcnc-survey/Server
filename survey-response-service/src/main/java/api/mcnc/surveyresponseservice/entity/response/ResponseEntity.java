package api.mcnc.surveyresponseservice.entity.response;

import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyresponseservice.service.request.UpdateCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  public Integer orderNumber() {
    return this.orderNumber;
  }

  @Builder
  private ResponseEntity(String id, String surveyId, String respondentId, String questionId, QuestionType questionType, Integer orderNumber, String response) {
    this.id = id;
    this.surveyId = surveyId;
    this.respondentId = respondentId;
    this.questionId = questionId;
    this.questionType = questionType;
    this.orderNumber = orderNumber;
    this.response = response;
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
      .build();
  }

  public void update(UpdateCommand updateCommand) {
    this.response = updateCommand.response();
  }

  public Response toDomain() {
    return Response.builder()
      .id(id)
      .surveyId(surveyId)
      .respondentId(respondentId)
      .questionId(questionId)
      .questionType(questionType)
      .orderNumber(orderNumber)
      .response(response)
      .build();
  }

  public void updateType(QuestionType questionType) {
    this.questionType = questionType;
  }
}
