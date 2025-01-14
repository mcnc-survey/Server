package api.mcnc.surveyservice.repository.survey;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-25 오후 3:16
 */
@Repository
public class DeleteSurveyRepository {

  private final SurveyJpaRepository surveyJpaRepository;
  private final TransactionOperations writeTransactionOperations;

  public DeleteSurveyRepository(
    SurveyJpaRepository surveyJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations) {
    this.surveyJpaRepository = surveyJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public void deleteSurvey(String surveyId) {
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyJpaRepository.deleteById(surveyId)
    );
  }


  public void deleteSurveyList(String adminId, List<String> surveyIds) {
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyJpaRepository.deleteAllByIdAndAdminId(adminId, surveyIds)
    );
  }
}
