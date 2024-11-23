package api.mcnc.surveyservice.entity.survey;

import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyservice.entity.question.QuestionEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 9:52
 */
@Entity
@Table(name = "surveys")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyEntity extends MutableBaseEntity {
  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "ADMIN_ID")
  private String adminId;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private SurveyStatus status;
  @Column(name = "START_AT")
  private LocalDateTime startAt;
  @Column(name = "END_AT")
  private LocalDateTime endAt;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "surveyId", cascade = CascadeType.ALL, orphanRemoval = true)
  List<QuestionEntity> questions;

  public Survey toDomain() {
    List<Question> questionList = questions.stream().map(QuestionEntity::toDomain).toList();
    return Survey.builder()
      .id(id)
      .adminId(adminId)
      .title(title)
      .description(description)
      .question(questionList)
      .status(status)
      .startAt(startAt)
      .endAt(endAt)
      .build();
  }

  public static SurveyEntity fromDomain(Survey survey) {
    List<QuestionEntity> questionEntityList = survey.question().stream().map(QuestionEntity::fromDomain).toList();
    return SurveyEntity.builder()
      .id(UUID.randomUUID().toString())
      .adminId(survey.adminId())
      .title(survey.title())
      .description(survey.description())
      .questions(questionEntityList)
      .status(survey.status())
      .startAt(survey.startAt())
      .endAt(survey.endAt())
      .build();
  }

  public boolean isEqualsAdminId(String adminId) {
    return this.adminId.equals(adminId);
  }
}
