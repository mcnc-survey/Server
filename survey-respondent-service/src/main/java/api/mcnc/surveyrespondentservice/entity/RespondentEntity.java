package api.mcnc.surveyrespondentservice.entity;

import api.mcnc.surveyrespondentservice.domain.Respondent;
import api.mcnc.surveyrespondentservice.entity.audit.BaseEntity;
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
 * @since :2024-11-20 오후 10:13
 */
@Entity
@Table(name = "RESPONDENTS")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

  public Respondent toDomain() {
    return Respondent.builder()
      .id(id)
      .name(name)
      .email(email)
      .phoneNumber(phoneNumber)
      .provider(provider)
      .surveyId(surveyId)
      .build();
  }

  public static RespondentEntity fromDomain(Respondent respondent) {
    return RespondentEntity.builder()
      .id(UUID.randomUUID().toString())
      .name(respondent.name())
      .email(respondent.email())
      .phoneNumber(respondent.phoneNumber())
      .provider(respondent.provider())
      .surveyId(respondent.surveyId())
      .build();
  }
}
