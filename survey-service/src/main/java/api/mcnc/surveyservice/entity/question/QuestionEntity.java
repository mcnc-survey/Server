package api.mcnc.surveyservice.entity.question;

import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 1:27
 */
@Entity
@Table(name = "questions")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends MutableBaseEntity {
  @Id @Getter
  @Column(name = "ID")
  private String id;
  @JoinColumn(name = "SURVEY_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private SurveyEntity survey;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private QuestionType questionType;
  @Column(name = "ORDER_NUMBER")
  private Integer order;
  @Column(name = "COLUMNS")
  private String columns;
  @Column(name = "ROWS")
  private String rows;
  @Column(name = "REQUIRED")
  private Boolean required;
  @Column(name = "ETC")
  private Boolean etc;

  public Question toDomain() {
    return Question.builder()
      .id(id)
      .questionType(questionType)
      .title(title)
      .order(order)
      .columns(columns)
      .rows(rows)
      .required(required)
      .etc(etc)
      .build();
  }

  public static QuestionEntity fromDomain(Question question) {
    return QuestionEntity.builder()
      .id(UUID.randomUUID().toString())
      .questionType(question.questionType())
      .title(question.title())
      .order(question.order())
      .columns(question.columns())
      .rows(question.rows())
      .required(question.required())
      .etc(question.etc())
      .build();
  }

  public void updateFrom(Question question) {
    this.title = question.title();
    this.questionType = question.questionType();
    this.order = question.order();
    this.columns = question.columns();
    this.rows = question.rows();
    this.required = question.required();
    this.etc = question.etc();
  }

  public void addSurvey(SurveyEntity surveyEntity) {
    this.survey = surveyEntity;
  }
}
