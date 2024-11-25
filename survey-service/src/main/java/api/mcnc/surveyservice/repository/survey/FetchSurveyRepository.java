package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

  public List<Survey> fetchAllByAdminIdForDelete(String adminId) {
    return readTransactionOperations.execute(status ->
      surveyJpaRepository
        .findAllByAdminIdAndStatus(adminId, SurveyStatus.DELETE)
        .stream()
        .map(SurveyEntity::toDomain)
        .toList()
    );
  }

  public Optional<Survey> fetchBySurveyIdAndAdminId(String surveyId, String adminId) {
    return readTransactionOperations.execute(status ->
      surveyJpaRepository
        .findByIdAndAdminId(surveyId, adminId)
        .map(SurveyEntity::toDomain)
    );
  }

  public Boolean existsBySurveyId(String id) {
    return readTransactionOperations.execute(status ->
      surveyJpaRepository.existsById(id)
    );
  }

}
