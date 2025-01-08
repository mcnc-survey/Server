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
    responses.forEach(response -> this.validate(response.response(), response.isRequired(), response.questionType(), response.etc()));
  }

  // QuestionResponseUpdate 검증
  public void validateResponseUpdates(List<QuestionResponseUpdate> responses) {
    responses.forEach(response -> this.validate(response.response(), response.isRequired(), response.questionType(), response.etc()));
  }

  private void validate(String response, boolean isRequired, QuestionType type, String etc) {
    // 필수 응답인데 response와 etc 둘 다 비어있는건 허용 안함
    if (isNotHasResponseWhenIsRequire(response, etc, isRequired)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "필수 응답은 비어있을 수 없습니다.");
    }

    // 빈 문자열은 허용 안함
    if (isBlank(response, etc)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "빈 문자열은 보낼 수 없습니다.");
    }

    // 기타항목과 일반 항목 동시 선택은 다중 선택만 가능하게
    if (!(QuestionType.MULTIPLE_CHOICE.equals(type)) && isHasTextSameTimeTwoResponseType(response, etc)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "두 개 이상의 응답 유형을 동시에 보낼 수 없습니다.");
    }

    // 단답 서술은 기타 항목 존재 불가능
    if(isTypeStringHasEtc(type, etc)){
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "단답, 서술은 기타 항목이 존재할 수 없습니다.");
    }

    // 기타에만 응답한 경우
    if (response == null) {
      return;
    }

    // 다중 선택이 아닌데 구분자 들어가 있으면 안됨
    if (!QuestionType.MULTIPLE_CHOICE.equals(type)) {
      if (response.contains(SEPARATOR)) {
        throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "잘못된 요청 유형입니다.");
      }
      return;
    }

    if (response.contains(SEPARATOR + SEPARATOR) || response.startsWith(SEPARATOR) || response.endsWith(SEPARATOR)) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST, "잘못된 요청 유형입니다.");
    }

  }

  private boolean isTypeStringHasEtc(QuestionType type, String etc) {
    return (QuestionType.SHORT_ANSWER.equals(type) || QuestionType.LONG_ANSWER.equals(type)) && etc != null;
  }

  private boolean isHasTextSameTimeTwoResponseType(String response, String etc) {
    return StringUtils.hasText(response) && StringUtils.hasText(etc);
  }

  private boolean isBlank(String response, String etc) {
    return (response != null && response.isBlank()) || (etc != null && etc.isBlank());
  }

  private boolean isNotHasResponseWhenIsRequire(String response, String etc, boolean isRequired) {
    return isRequired && !StringUtils.hasText(response) && !StringUtils.hasText(etc);
  }

}
