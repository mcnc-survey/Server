package api.mcnc.surveyresponseservice.repository.delete;

import api.mcnc.surveyresponseservice.entity.delete.DeleteQueueEntity;
import api.mcnc.surveyresponseservice.repository.response.ResponseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오후 8:16
 */
@Repository
public class DeleteQueueRepository {

  private final DeleteQueueJpaRepository deleteQueueJpaRepository;
  private final ResponseJpaRepository responseJpaRepository;
  private final TransactionOperations writeTransactionOperations;

  public DeleteQueueRepository(
    ResponseJpaRepository responseJpaRepository,
    DeleteQueueJpaRepository deleteQueueJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations
  ) {
    this.responseJpaRepository = responseJpaRepository;
    this.deleteQueueJpaRepository = deleteQueueJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public void waitDeletion(List<String> surveyIdList) {
    writeTransactionOperations.executeWithoutResult(transactionStatus -> {
      List<DeleteQueueEntity> waitList = surveyIdList.stream().map(DeleteQueueEntity::of).toList();
      deleteQueueJpaRepository.saveAll(waitList);
    });
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void deleteResponse() {
    writeTransactionOperations.executeWithoutResult(transactionStatus -> {
      List<DeleteQueueEntity> waitList = deleteQueueJpaRepository.findAll();
      List<String> surveyIdList = waitList.stream().map(DeleteQueueEntity::getSurveyId).toList();
      responseJpaRepository.deleteIn(surveyIdList);
      deleteQueueJpaRepository.deleteAll();
    });
  }

}
