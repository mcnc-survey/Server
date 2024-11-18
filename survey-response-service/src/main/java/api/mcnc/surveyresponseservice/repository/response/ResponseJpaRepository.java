package api.mcnc.surveyresponseservice.repository.response;

import api.mcnc.surveyresponseservice.entity.response.ResponseEntity;
import org.springframework.data.repository.ListCrudRepository;
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

  // 내가 한 설문 응답
  List<ResponseEntity> findAllBySurveyIdAndRespondentIdOrderByOrderNumber(String surveyId, String respondentId);

  // id에 해당하는 항목들만 가져오기
  List<ResponseEntity> findAllByIdIn(Set<String> ids);
}
