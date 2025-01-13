package api.mcnc.surveyrespondentservice.repository;

import api.mcnc.surveyrespondentservice.entity.RespondentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 레파지토리
 *
 * @author :유희준
 * @since :2024-11-22 오전 9:17
 */
public interface RespondentJpaRepository extends CrudRepository<RespondentEntity, String> {
  boolean existsByEmailAndSurveyId(String email, String surveyId);
  Optional<RespondentEntity> findByEmailAndSurveyId(String email, String surveyId);
}
