package api.mcnc.surveyresponseservice.service.validation;

import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오전 9:44
 */
@Component
public class ResponseValidator {

  private static final String SEPARATOR = "|`|";

  // 기존 QuestionResponse 검증
  public void validateResponses(List<QuestionResponse> responses) {
    responses.forEach(response -> this.validate(response.response(), response.isRequired(), response.questionType()));
  }

  // QuestionResponseUpdate 검증
  public void validateResponseUpdates(List<QuestionResponseUpdate> responses) {
    responses.forEach(response -> this.validate(response.response(), response.isRequired(), response.questionType()));
  }

  private void validate(String response, boolean isRequired, QuestionType type) {
    if (isRequired && !StringUtils.hasText(response)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "필수 응답은 비어있을 수 없습니다.");
    }

    if (!QuestionType.MULTIPLE_CHOICE.equals(type)) {
      return;
    }

    if (response.contains(SEPARATOR + SEPARATOR) || response.startsWith(SEPARATOR) || response.endsWith(SEPARATOR)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "잘못된 요청 유형입니다.");
    }

  }

}
