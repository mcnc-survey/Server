package api.mcnc.surveyresponseservice.repository.response;

import api.mcnc.surveyresponseservice.entity.response.ResponseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 10:15
 */
@Repository
public interface ResponseJpaRepository extends ListCrudRepository<ResponseEntity, String> {

  // 응답 한 적 있는지
  boolean existsBySurveyIdAndRespondentId(String surveyId, String respondentId);

  // 설문 ID에 해당하는 모든 응답을 반환
  List<ResponseEntity> findAllBySurveyId(String surveyId);
  List<ResponseEntity> findAllBySurveyIdOrderByOrderNumber(String surveyId);

  // 내가 한 설문 응답
  List<ResponseEntity> findAllBySurveyIdAndRespondentIdOrderByOrderNumber(String surveyId, String respondentId);

  // id에 해당하는 항목들만 가져오기
  List<ResponseEntity> findAllByIdIn(Set<String> ids);

  // 설문에 해당하는 총 응답자 수
  @Query("SELECT COUNT(DISTINCT r.respondentId) FROM ResponseEntity r WHERE r.surveyId = :surveyId")
  Integer countTotalRespondentsBySurveyId(String surveyId);

  @Modifying
  @Query("DELETE FROM ResponseEntity r WHERE r.surveyId IN :surveyIdList")
  void deleteIn(@Param("surveyIdList") List<String> surveyIdList);

}
