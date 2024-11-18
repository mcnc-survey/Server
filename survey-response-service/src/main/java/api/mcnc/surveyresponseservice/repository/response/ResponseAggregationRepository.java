package api.mcnc.surveyresponseservice.repository.response;

import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.entity.response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 9:12
 */
@Repository
@RequiredArgsConstructor
public class ResponseAggregationRepository {
  private final ResponseJpaRepository responseJpaRepository;
  private final TransactionOperations readTransactionOperations;

  // 질문 항목 응답 데이터
  public Map<Integer, List<Response>> getResponseListMappingByOrderNumberBySurveyId(String surveyId) {
    return readTransactionOperations.execute(status ->
        responseJpaRepository.findAllBySurveyId(surveyId)
          .parallelStream()
//        orderNumber 별 응답 리스트
          .collect(Collectors.groupingBy(
            ResponseEntity::orderNumber,
            Collectors.mapping(
              ResponseEntity::toDomain,
              Collectors.toList()
            )
          ))

    );
  }
}
