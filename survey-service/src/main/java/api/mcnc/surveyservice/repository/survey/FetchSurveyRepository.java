package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오전 10:54
 */
@Repository
public class FetchSurveyRepository {
  private final SurveyJpaRepository surveyJpaRepository;
  private final TransactionOperations readTransactionOperations;

  public FetchSurveyRepository(
    @Qualifier("readTransactionOperations")
    TransactionOperations readTransactionOperations,
    SurveyJpaRepository surveyJpaRepository
  ) {
    this.readTransactionOperations = readTransactionOperations;
    this.surveyJpaRepository = surveyJpaRepository;
  }

  public List<Survey> fetchAllByAdminId(String adminId) {
    return readTransactionOperations.execute(status ->
      surveyJpaRepository
        .findAllByAdminId(adminId)
        .stream()
        .map(SurveyEntity::toDomain)
        .toList()
    );
  }

}
