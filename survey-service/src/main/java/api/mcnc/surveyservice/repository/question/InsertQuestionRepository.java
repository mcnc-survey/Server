package api.mcnc.surveyservice.repository.question;

import api.mcnc.surveyservice.domain.Question;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionOperations;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 5:55
 */
@Repository
public class InsertQuestionRepository {
  private final QuestionJpaRepository questionJpaRepository;
  private final TransactionOperations writeTransactionOperations;

  public InsertQuestionRepository(
    QuestionJpaRepository questionJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations) {
    this.questionJpaRepository = questionJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public void create(Question question) {

  }
}
