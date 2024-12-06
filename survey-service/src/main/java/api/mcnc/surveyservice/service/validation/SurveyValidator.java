package api.mcnc.surveyservice.service.validation;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.entity.question.QuestionType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오전 9:44
 */
@Component
public class SurveyValidator {
  // 기존 QuestionResponse 검증
  public void validateResponses(List<QuestionCreateRequest> requests) {
    requests.forEach(this::validateQuestionResponse);
  }

  // QuestionResponseUpdate 검증
  public void validateResponseUpdates(List<SurveyUpdateRequest.Question> requests) {
    requests.forEach(this::validateQuestionResponseUpdate);
  }

  private void validateQuestionResponse(QuestionCreateRequest request) {
    if (!isValidResponseFormat(request.questionType(), request.columns(), request.rows())) {
      throw new SurveyException(SurveyErrorCode.INVALID_REQUEST,
        String.format("[%s] - [col: [%s], row: [%s]] 두 항목의 유형이 일치하지 않습니다.",
          request.questionType(),
          request.columns(),
          request.rows()
        )
      );
    }
  }

  private void validateQuestionResponseUpdate(SurveyUpdateRequest.Question request) {
    if (!isValidResponseFormat(request.questionType(), request.columns(), request.rows())) {
      throw new SurveyException(SurveyErrorCode.INVALID_REQUEST,
        String.format("[%s] - [col: [%s], row: [%s]] 두 항목의 유형이 일치하지 않습니다.",
          request.questionType(),
          request.columns(),
          request.rows()
        )
      );
    }
  }

  private boolean isValidResponseFormat(QuestionType type, String columns, String rows) {
    if (StringUtils.hasText(columns)) {
      return false;
    }

    return switch (type) {
      case SINGLE_CHOICE -> validateSingleChoice(columns);
      case MULTIPLE_CHOICE -> validateMultipleChoice(columns);
      case SHORT_ANSWER, LONG_ANSWER -> StringUtils.hasText(columns); // 빈 문자열만 아니면 됨
      case TABLE_SELECT -> validateTableSelect(columns, rows);
    };
  }

  private boolean validateSingleChoice(String columns) {
    try {
      int value = Integer.parseInt(columns.trim());
      return value > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateMultipleChoice(String columns) {
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (columns.contains(",,") || columns.startsWith(",") || columns.endsWith(",")) {
      return false;
    }
    String[] choices = columns.split(",");
    try {
      return Arrays.stream(choices)
        .map(Integer::parseInt)
        .allMatch(value -> value > 0);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateTableSelect(String columns, String rows) {
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (columns.contains(",,") || columns.startsWith(",") || columns.endsWith(",")) {
      return false;
    }

    String[] selections = columns.split(",");
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
