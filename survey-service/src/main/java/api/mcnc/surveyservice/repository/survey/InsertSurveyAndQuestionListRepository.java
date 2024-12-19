package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.question.QuestionEntity;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 4:26
 */
@Repository
public class InsertSurveyAndQuestionListRepository {

  private final EntityManager entityManager;
  private final TransactionOperations writeTransactionOperations;

  public InsertSurveyAndQuestionListRepository(
    EntityManager entityManager,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations) {
    this.entityManager = entityManager;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public String createSurvey(Survey survey, List<Question> questionList) {
    return writeTransactionOperations.execute(status -> {
      SurveyEntity surveyEntity = SurveyEntity.fromDomain(survey);
      List<QuestionEntity> questionEntityList = questionList.stream().map(QuestionEntity::fromDomain).toList();
      surveyEntity.addQuestions(questionEntityList);
      entityManager.persist(surveyEntity);
      entityManager.flush();
      return surveyEntity.getId();
    });
  }

}
