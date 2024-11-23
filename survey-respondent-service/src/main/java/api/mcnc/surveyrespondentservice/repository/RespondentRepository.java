package api.mcnc.surveyrespondentservice.repository;

import api.mcnc.surveyrespondentservice.domain.Respondent;
import api.mcnc.surveyrespondentservice.entity.RespondentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 5:01
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class RespondentRepository {
  private final RespondentJpaRepository respondentJpaRepository;

  public Respondent register(Respondent respondent) {
    RespondentEntity respondentEntity = RespondentEntity.fromDomain(respondent);
    RespondentEntity save = respondentJpaRepository.save(respondentEntity);
    return save.toDomain();
  }

  public boolean isExist(String email, String surveyId) {
    return respondentJpaRepository.existsByEmailAndSurveyId(email, surveyId);
  }

  public Optional<Respondent> getRespondent(String email, String surveyId) {
    return respondentJpaRepository
      .findByEmailAndSurveyId(email, surveyId)
      .map(RespondentEntity::toDomain);
  }
}
