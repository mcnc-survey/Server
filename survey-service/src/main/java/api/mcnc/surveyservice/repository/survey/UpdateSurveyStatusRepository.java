package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.time.LocalDateTime;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 9:02
 */
@Repository
public class UpdateSurveyStatusRepository {

  private final TransactionOperations writeTransactionOperations;
  private final SurveyJpaRepository surveyRepository;

  public UpdateSurveyStatusRepository(
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations,
    SurveyJpaRepository surveyRepository) {
    this.writeTransactionOperations = writeTransactionOperations;
    this.surveyRepository = surveyRepository;
  }

  public void updateSurveyStatusToBeginEdit(String surveyId) {
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyRepository.updateSurveyStatus(surveyId, SurveyStatus.EDIT)
    );
  }

  // 설문 시작 시간, 마감 시간에 따라 상태가 수정 후 상태가 달라져서 STATUS를 입력받음
  public void updateSurveyStatusToEndEdit(String surveyId, SurveyStatus status) {
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyRepository.updateSurveyStatus(surveyId, status)
    );
  }

  public void updateSurveyStatusToDelete(String surveyId) {
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyRepository.updateSurveyStatus(surveyId, SurveyStatus.DELETE)
    );
  }

  public void updateSurveyStatusToStart(SurveyStatus status, SurveyStatus currentStatus, LocalDateTime now){
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyRepository.updateSurveyStatusToStart(status, currentStatus, now)
    );

  }

  public void updateSurveyStatusToEnd(SurveyStatus status, SurveyStatus currentStatus, LocalDateTime now){
    writeTransactionOperations.executeWithoutResult(execute ->
      surveyRepository.updateSurveyStatusToEnd(status, currentStatus, now)
    );
  }

}
