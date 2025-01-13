package api.mcnc.surveyservice.repository.question;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.question.QuestionEntity;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.repository.survey.FetchSurveyRepository;
import api.mcnc.surveyservice.repository.survey.SurveyJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-19 오후 1:45
 */
@Repository
public class FetchQuestionRepository {
  private final QuestionJpaRepository questionJpaRepository;
  private final SurveyJpaRepository surveyJpaRepository;
  private final TransactionOperations readTransactionOperations;

  public FetchQuestionRepository(
    @Qualifier("readTransactionOperations") TransactionOperations readTransactionOperations,
    QuestionJpaRepository questionJpaRepository,
    SurveyJpaRepository surveyJpaRepository
  ) {
    this.questionJpaRepository = questionJpaRepository;
    this.readTransactionOperations = readTransactionOperations;
    this.surveyJpaRepository = surveyJpaRepository;
  }

  public List<Question> fetchAllBySurveyId(String surveyId, String adminId) {
    return readTransactionOperations.execute(status ->{
      SurveyEntity surveyEntity = surveyJpaRepository.findById(surveyId).orElseThrow(() -> new SurveyException(SurveyErrorCode.FOUND_NOT_SURVEY));
      return questionJpaRepository
          .findAllBySurvey(surveyEntity)
          .stream()
          .map(QuestionEntity::toDomain)
          .toList();
      }
    );
  }

  public boolean existsBySurveyId(String surveyId) {
    return Boolean.TRUE.equals(readTransactionOperations.execute(status ->
      questionJpaRepository.existsBySurveyId(surveyId)
    ));
  }



}
