package api.mcnc.surveyresponseservice.service.validation;

import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오전 9:44
 */
@Component
public class ResponseValidator {
  // 기존 QuestionResponse 검증
  public void validateResponses(List<QuestionResponse> responses) {
    responses.forEach(this::validateQuestionResponse);
  }

  // QuestionResponseUpdate 검증
  public void validateResponseUpdates(List<QuestionResponseUpdate> responses) {
    responses.forEach(this::validateQuestionResponseUpdate);
  }

  private void validateQuestionResponse(QuestionResponse response) {
    if (!isValidResponseFormat(response.questionType(), response.response())) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST,
        String.format("[%s] - [%s] 두 항목의 유형이 일치하지 않습니다.",
          response.questionType(),
          response.response()
        )
      );
    }
  }

  private void validateQuestionResponseUpdate(QuestionResponseUpdate response) {
    if (!isValidResponseFormat(response.questionType(), response.response())) {
      throw new ResponseException(ResponseErrorCode.INVALID_REQUEST,
        String.format("[%s] - [%s] 두 항목의 유형이 일치하지 않습니다.",
          response.questionType(),
          response.response()
        )
      );
    }
  }

  private boolean isValidResponseFormat(QuestionType type, String response) {
    if (response == null || response.trim().isEmpty()) {
      return false;
    }

    return switch (type) {
      case SINGLE_CHOICE -> validateSingleChoice(response);
      case MULTIPLE_CHOICE -> validateMultipleChoice(response);
      case SHORT_ANSWER -> validateShortAnswer(response);
      case TABLE_SELECT -> validateTableSelect(response);
    };
  }

  private boolean validateSingleChoice(String response) {
    try {
      int value = Integer.parseInt(response.trim());
      return value > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateMultipleChoice(String response) {
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (response.contains(",,") || response.startsWith(",") || response.endsWith(",")) {
      return false;
    }
    String[] choices = response.split(",");
    try {
      return Arrays.stream(choices)
        .map(Integer::parseInt)
        .allMatch(value -> value > 0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateShortAnswer(String response) {
    return !response.trim().isEmpty();
  }

  private boolean validateTableSelect(String response) {
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (response.contains(",,") || response.startsWith(",") || response.endsWith(",")) {
      return false;
    }

    String[] selections = response.split(",");
    try {
      return Arrays.stream(selections)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .allMatch(selection -> {
          String[] parts = selection.split(":");
          if (parts.length != 2) return false;

          int row = Integer.parseInt(parts[0].trim());
          int col = Integer.parseInt(parts[1].trim());
          return row > 0 && col > 0;
        });
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
