package api.mcnc.surveyresponseservice.service.response;

import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import api.mcnc.surveyresponseservice.service.response.strategy.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 6:46
 */
@Component
public class ResponseResultByQuestionType {
  private QuestionTypeIfs questionType;

  public Object calculateResponseResult(List<Response> values) {
    QuestionType currentQuestionType = values.get(0).questionType();
    setQuestionType(currentQuestionType);
    return questionType.calculateResponseResult(values);
  }

  private void setQuestionType(QuestionType questionType) {
    switch (questionType) {
      case SINGLE_CHOICE:
        this.questionType = new SingleChoiceType();
        break;
      case MULTIPLE_CHOICE:
        this.questionType = new MultipleChoiceType();
        break;
      case SHORT_ANSWER: case LONG_ANSWER:
        this.questionType = new StringAnswerType();
        break;
      case TABLE_SELECT:
        this.questionType = new TableSelectType();
        break;
      default:
        throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, String.format("[%s]는 존재 하지 않는 형식 입니다.", questionType));
    }
  }
}
