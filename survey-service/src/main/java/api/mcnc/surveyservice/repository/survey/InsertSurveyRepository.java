package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 4:26
 */
@Repository
public class InsertSurveyRepository {

  private final SurveyJpaRepository surveyJpaRepository;
  private final TransactionOperations writeTransactionOperations;

  public InsertSurveyRepository(
    SurveyJpaRepository surveyJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations) {
    this.surveyJpaRepository = surveyJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public void create(Survey survey) {
    writeTransactionOperations.executeWithoutResult(status -> {
      SurveyEntity surveyEntity = SurveyEntity.fromDomain(survey);
      surveyJpaRepository.save(surveyEntity);
    });
  }

}
