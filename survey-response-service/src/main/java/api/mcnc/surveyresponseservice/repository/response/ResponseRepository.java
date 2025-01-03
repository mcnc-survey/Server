package api.mcnc.surveyresponseservice.repository.response;

import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.controller.request.ResponseUpdate;
import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import api.mcnc.surveyresponseservice.entity.response.ResponseEntity;
import api.mcnc.surveyresponseservice.service.request.UpdateCommand;
import api.mcnc.surveyresponseservice.service.request.UpdateTypeCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-14 오후 1:00
 */
@Repository
public class ResponseRepository {

  private final ResponseJpaRepository responseJpaRepository;
  private final TransactionOperations writeTransactionOperations;
  private final TransactionOperations readTransactionOperations;

  public ResponseRepository(
    ResponseJpaRepository responseJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations,
    @Qualifier("readTransactionOperations")
    TransactionOperations readTransactionOperations) {
    this.responseJpaRepository = responseJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
    this.readTransactionOperations = readTransactionOperations;
  }

  // 설문 응답
  public void responseSurvey(String surveyId, String respondentId, List<Response> response) {
    writeTransactionOperations.executeWithoutResult(status -> {
//      응답 한 적 있으면 안됨
      if (isResponseExist(surveyId, respondentId)) {
        throw new ResponseException(ResponseErrorCode.DUPLICATE_RESPONSE);
      }
      
      List<ResponseEntity> responseList = response.stream()
        .map(ResponseEntity::of)
        .toList();

      responseJpaRepository.saveAll(responseList);
    });
  }

  // 설문 응답 수정
  public void updateResponse(String surveyId, String respondentId, Map<String, UpdateCommand> updateMap) {
    writeTransactionOperations.executeWithoutResult(status -> {
      // 응답 한 적 없는 설문은 수정 불가
      if (!(isResponseExist(surveyId, respondentId))) {
        throw new ResponseException(ResponseErrorCode.NOT_FOUND_RESPONSE);
      }

      // update가 필요한 응답 항목만 가져옴
      Set<String> ids = updateMap.keySet();
      List<ResponseEntity> responseList = responseJpaRepository.findAllByIdIn(ids);

      // response의 각 UpdateCommand 객체를 순회하면서 일치하는 엔티티를 업데이트
      boolean isUpdated = false;
      for (ResponseEntity target : responseList) {
        // 수정할 내용 가져오기
        UpdateCommand update = updateMap.get(target.getId());
        if (update != null) {
          // 필요한 필드를 업데이트
          target.update(update);
          isUpdated = true;
        }
      }

      // 업데이트가 있었으면 saveAll 호출
      if (isUpdated) {
        responseJpaRepository.saveAll(responseList);  // 변경된 엔티티만 저장
      }
    });
  }

  // 내가 한 설문 응답 전체 가져오기
  public List<Response> getRespondentResponseList(String surveyId, String respondentId) {
    return readTransactionOperations.execute(status ->
      responseJpaRepository.findAllBySurveyIdAndRespondentIdOrderByOrderNumber(surveyId, respondentId)
        .stream()
        .map(ResponseEntity::toDomain)
        .toList()
    );
  }

  public void updateType(String surveyId, List<UpdateTypeCommand> question) {
    writeTransactionOperations.executeWithoutResult(status -> {
      List<ResponseEntity> responseList = responseJpaRepository.findAllBySurveyId(surveyId);
      for (ResponseEntity response : responseList) {
        for (UpdateTypeCommand update : question) {
          if (response.getQuestionId().equals(update.questionId())) {
            response.updateType(update.type());
          }
        }
      }
      responseJpaRepository.saveAll(responseList);
    });
  }

  // 응답 한 적 있는 설문 인지
  private boolean isResponseExist(String surveyId, String respondentId) {
    return responseJpaRepository.existsBySurveyIdAndRespondentId(surveyId, respondentId);
  }

}
