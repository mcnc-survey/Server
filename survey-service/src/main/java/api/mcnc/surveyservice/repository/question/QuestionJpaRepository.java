package api.mcnc.surveyservice.repository.question;

import api.mcnc.surveyservice.entity.question.QuestionEntity;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-19 오후 1:43
 */
public interface QuestionJpaRepository extends ListCrudRepository<QuestionEntity, String> {
  List<QuestionEntity> findAllBySurvey(SurveyEntity survey);
  boolean existsBySurveyId(String surveyId);
}
