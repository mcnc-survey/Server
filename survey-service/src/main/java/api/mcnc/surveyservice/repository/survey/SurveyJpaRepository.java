package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 10:52
 */
public interface SurveyJpaRepository extends CrudRepository<SurveyEntity, String> {
  List<SurveyEntity> findAllByAdminId(String adminId);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.status = :currentStatus AND s.startAt <= :now")
  void updateSurveyStatusToStart(@Param("status") SurveyStatus status, @Param("currentStatus") SurveyStatus currentStatus, @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.status = :currentStatus AND s.endAt <= :now")
  void updateSurveyStatusToEnd(@Param("status") SurveyStatus status, @Param("currentStatus") SurveyStatus currentStatus, @Param("now") LocalDateTime now);

}