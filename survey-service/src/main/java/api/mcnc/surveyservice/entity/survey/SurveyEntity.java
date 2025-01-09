package api.mcnc.surveyservice.entity.survey;

import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.audit.MutableBaseEntity;
import api.mcnc.surveyservice.entity.question.QuestionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
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
  @Id @Getter
  @Column(name = "ID")
  private String id;
  @Getter
  @Column(name = "ADMIN_ID")
  private String adminId;
  @Getter
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
  @Column(name = "SURVEY_LIKE")
  @Enumerated(EnumType.STRING)
  private SurveyLike like;
  @Getter
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
  List<QuestionEntity> questions;

  public static SurveyEntity fromDomain(Survey survey) {
    return SurveyEntity.builder()
      .id(UUID.randomUUID().toString())
      .adminId(survey.getAdminId())
      .title(survey.getTitle())
      .description(survey.getDescription())
      .status(survey.getStatus())
      .startAt(survey.getStartAt())
      .endAt(survey.getEndAt())
      .like(SurveyLike.DISLIKE)
      .build();
  }

  public Survey toDomain() {
    List<Question> questionList = questions.stream()
      .map(QuestionEntity::toDomain)
      .sorted(Comparator.comparingInt(Question::getOrder))
      .toList();
    return Survey.builder()
      .id(id)
      .adminId(adminId)
      .title(title)
      .description(description)
      .question(questionList)
      .status(status)
      .startAt(startAt)
      .endAt(endAt)
      .like(like)
      .modifiedAt(modifiedAt)
      .build();
  }

  public void updateFrom(Survey survey) {
    this.title = survey.getTitle();
    this.description = survey.getDescription();
    this.status = survey.getStatus();
    this.startAt = survey.getStartAt();
    this.endAt = survey.getEndAt();
  }

  public void updateLikeUpdate() {
    if (SurveyLike.LIKE.equals(this.like)) {
      this.like = SurveyLike.DISLIKE;
    } else {
      this.like = SurveyLike.LIKE;
    }
  }

  public void addQuestions(List<QuestionEntity> questionEntityList) {
    questionEntityList.forEach(questionEntity -> questionEntity.addSurvey(this));
    this.questions = questionEntityList;
  }

  public boolean isEqualsAdminId(String adminId) {
    return this.adminId.equals(adminId);
  }
}
