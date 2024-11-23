package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 10:52
 */
public interface SurveyJpaRepository extends CrudRepository<SurveyEntity, String> {
  List<SurveyEntity> findAllByAdminId(String adminId);
}
