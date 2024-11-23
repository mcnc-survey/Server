package api.mcnc.surveyservice.entity.question;

import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
public class QuestionEntity {
  @Id
  @Column(name = "ID")
  private String id;
  @JoinColumn(name = "SURVEY_ID")
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private SurveyEntity survey;
  @Column(name = "TYPE")
  private String questionType;
  @Column(name = "ORDER")
  private Integer order;
  @Column(name = "COLUMNS")
  private String columns;
  @Column(name = "ROWS")
  private String rows;

  public Question toDomain() {
    return Question.builder()
      .id(id)
      .questionType(questionType)
      .order(order)
      .columns(columns)
      .rows(rows)
      .build();
  }

  public static QuestionEntity fromDomain(Question question) {
    return QuestionEntity.builder()
      .id(UUID.randomUUID().toString())
      .questionType(question.questionType())
      .order(question.order())
      .columns(question.columns())
      .rows(question.rows())
      .build();
  }

}
