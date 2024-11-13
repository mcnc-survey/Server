package api.mcnc.surveyresponseservice.entity.answer;

import api.mcnc.surveyresponseservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyresponseservice.entity.respondent.RespondentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 10:00
 */
@Entity
@Table(name = "answers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends MutableBaseEntity {
  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "SURVEY_ID")
  private String surveyId;

  // 굳이 client 로 갈 필요 없는 정보
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "RESPONDENT_ID")
  private RespondentEntity respondent;

  @Column(name = "QUESTION_ID")
  private String questionId;
  @Enumerated(EnumType.STRING)
  @Column(name = "QUESTION_TYPE")
  private QuestionType questionType;
  @Column(name = "ORDER_NUMBER")
  private Integer orderNumber;
  @Column(name = "ANSWER")
  private String answer;
}
