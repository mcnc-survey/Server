package api.mcnc.surveyservice.repository.survey;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.domain.Question;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.entity.question.QuestionEntity;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
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
 * @since :2024-11-25 오전 11:29
 */
@Repository
public class UpdateSurveyRepository {
  private final SurveyJpaRepository surveyJpaRepository;
  private final TransactionOperations writeTransactionOperations;

  public UpdateSurveyRepository(
    SurveyJpaRepository surveyJpaRepository,
    @Qualifier("writeTransactionOperations")
    TransactionOperations writeTransactionOperations
  ) {
    this.surveyJpaRepository = surveyJpaRepository;
    this.writeTransactionOperations = writeTransactionOperations;
  }

  public Survey updateSurvey(String surveyId, Survey survey, List<Question> withId, List<Question> withoutId, Set<String> updateIds) {
    return writeTransactionOperations.execute(execute -> {
//    1. 기존 설문 조회
      SurveyEntity surveyEntity = surveyJpaRepository.findById(surveyId)
        .orElseThrow(() -> new SurveyException(SurveyErrorCode.INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다."));

//      2. 업데이트 대상이 아닌 기존 질문들 삭제
      surveyEntity.getQuestions().removeIf(question ->
        !updateIds.contains(question.getId())
      );
      surveyEntity.updateFrom(survey);

//      3. 기존 질문들을 Map으로 변환하여 조회
      Map<String, QuestionEntity> existingQuestions = surveyEntity.getQuestions()
        .stream()
        .collect(Collectors.toMap(QuestionEntity::getId, q -> q));

//      4. ID가 있는 질문들 업데이트
      withId.forEach(question -> {
        QuestionEntity existingQuestion = existingQuestions.get(question.id());
        if (existingQuestion != null) {
          existingQuestion.updateFrom(question);  // 기존 질문 업데이트
        }
      });

//      5. 새로운 질문들 생성 및 추가
      withoutId.forEach(question -> {
        QuestionEntity newQuestion = QuestionEntity.fromDomain(question);
        newQuestion.addSurvey(surveyEntity);
        surveyEntity.getQuestions().add(newQuestion);
      });

      return surveyEntity.toDomain();

    });
  }

  public void updateLike(String surveyId) {
    writeTransactionOperations.executeWithoutResult(execute -> {
      SurveyEntity surveyEntity = surveyJpaRepository.findById(surveyId)
        .orElseThrow(() -> new SurveyException(SurveyErrorCode.INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다."));

      surveyEntity.updateLikeUpdate();
    });

  }
}
