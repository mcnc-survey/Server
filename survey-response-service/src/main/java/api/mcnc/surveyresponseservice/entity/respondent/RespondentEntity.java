package api.mcnc.surveyresponseservice.entity.respondent;

import api.mcnc.surveyresponseservice.domain.respondent.Respondent;
import api.mcnc.surveyresponseservice.domain.respondent.RespondentCreateRequest;
import api.mcnc.surveyresponseservice.entity.answer.AnswerEntity;
import api.mcnc.surveyresponseservice.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 9:57
 */
@Entity
@Table(name = "respondents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RespondentEntity extends BaseEntity {
  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;
  @Column(name = "PROVIDER")
  private String provider;
  @Column(name = "SURVEY_ID")
  private String surveyId;

  @OneToMany(mappedBy = "respondent", fetch = FetchType.LAZY)
  private List<AnswerEntity> answers;

  @Builder
  private RespondentEntity(String id, String name, String email, String phoneNumber, String provider, String surveyId, List<AnswerEntity> answers) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.provider = provider;
    this.surveyId = surveyId;
    this.answers = answers;
  }

  public static RespondentEntity from(RespondentCreateRequest request) {
    return RespondentEntity.builder()
            .id(UUID.randomUUID().toString())
            .name(request.name())
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .provider(request.provider())
            .surveyId(request.surveyId())
            .build();
  }

  public Respondent toDomain() {
    return Respondent.builder()
            .id(id)
            .name(name)
            .email(email)
            .phoneNumber(phoneNumber)
            .provider(provider)
            .surveyId(surveyId)
            .answers(answers)
            .build();
  }

}
