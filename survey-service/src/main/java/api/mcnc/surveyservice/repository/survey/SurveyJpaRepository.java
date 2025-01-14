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
 * @author :유희준
 * @since :2024-11-19 오전 10:52
 */
public interface SurveyJpaRepository extends CrudRepository<SurveyEntity, String> {
  @Query("SELECT s FROM SurveyEntity s WHERE s.status != 'DELETE' AND s.adminId = :adminId")
  List<SurveyEntity> findAllByAdminId(String adminId);
  List<SurveyEntity> findAllByAdminIdAndStatus(String adminId, SurveyStatus status);

  boolean existsByIdAndStatus(String id, SurveyStatus status);
  boolean existsByIdAndAdminId(String id, String adminId);

  @Query("SELECT s FROM SurveyEntity s WHERE s.status != 'DELETE' AND s.like = 'LIKE' AND s.adminId = :adminId")
  List<SurveyEntity> findAllLikeSurveyByAdminId(String adminId);

  Optional<SurveyEntity> findByIdAndAdminId(String id, String adminId);

  @Modifying
  @Query(value = "UPDATE surveys s " +
    "SET status = :status " +
    "WHERE s.status != 'DELETE' AND" +
    " s.status = :currentStatus AND" +
    " s.start_at <= :now " +
    "RETURNING *"
    , nativeQuery = true
  )
  List<SurveyEntity> updateSurveyStatusToStart(@Param("status") String status, @Param("currentStatus") String currentStatus, @Param("now") LocalDateTime now);

  @Modifying
  @Query(value = "UPDATE surveys s " +
    "SET status = :status" +
    " WHERE s.status != 'DELETE' AND" +
    " s.status = :currentStatus AND" +
    " s.end_at <= :now " +
    "RETURNING *"
  ,nativeQuery = true)
  List<SurveyEntity> updateSurveyStatusToEnd(@Param("status") String status, @Param("currentStatus") String currentStatus, @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = :status WHERE s.id = :id")
  void updateSurveyStatus(@Param("id") String id, @Param("status") SurveyStatus status);

  @Modifying
  @Query("UPDATE SurveyEntity s SET s.status = 'WAIT' WHERE s.id in (:ids) AND s.adminId = :adminId AND s.status = 'DELETE'")
  void updateSurveyStatusToRestore(@Param("adminId") String adminId, @Param("ids") List<String> ids);

  @Modifying
  @Query("Delete from SurveyEntity s WHERE s.id in (:ids) AND s.adminId = :adminId AND s.status = 'DELETE'")
  void deleteAllByIdAndAdminId(@Param("adminId") String adminId, @Param("ids") List<String> ids);
}