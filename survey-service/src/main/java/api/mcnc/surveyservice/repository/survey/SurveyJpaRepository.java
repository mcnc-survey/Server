package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 10:52
 */
public interface SurveyJpaRepository extends CrudRepository<SurveyEntity, String> {
  List<SurveyEntity> findAllByAdminId(String adminId);
  List<SurveyEntity> findAllByAdminIdAndStatus(String adminId, SurveyStatus status);

  Optional<SurveyEntity> findByIdAndAdminId(String id, String adminId);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.status != 'EDIT' AND s.status != 'DELETE' AND s.status = :currentStatus AND s.startAt <= :now")
  void updateSurveyStatusToStart(@Param("status") SurveyStatus status, @Param("currentStatus") SurveyStatus currentStatus, @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.status != 'EDIT' AND s.status != 'DELETE' AND s.status = :currentStatus AND s.endAt <= :now")
  void updateSurveyStatusToEnd(@Param("status") SurveyStatus status, @Param("currentStatus") SurveyStatus currentStatus, @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.id = :id")
  void updateSurveyStatus(@Param("id") String id, @Param("status") SurveyStatus status);

}