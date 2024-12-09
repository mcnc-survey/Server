package api.mcnc.surveyservice.service.validation;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.entity.question.QuestionType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static api.mcnc.surveyservice.common.constants.SurveySeparator.SEPARATOR;

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

  private boolean isNotHasText(String columns) {
    return !StringUtils.hasText(columns);
  }

  private boolean isValidResponseFormat(QuestionType type, String columns, String rows) {
    return switch (type) {
      case SINGLE_CHOICE, MULTIPLE_CHOICE -> validateChoice(columns);
      case SHORT_ANSWER, LONG_ANSWER -> true; // title만 존재하고 나머지는 있던 말던 상관 없음
      case TABLE_SELECT -> validateTableSelect(columns, rows);
    };
  }

  private boolean validateChoice(String columns) {
    if (isNotHasText(columns)) {
      return false;
    }
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (columns.contains(SEPARATOR + SEPARATOR) || columns.startsWith(SEPARATOR) || columns.endsWith(SEPARATOR)) {
      return false;
    }

    return true;
  }
// TODO 2024-12-09 yhj : 표형 선택 도입 가능성
  private boolean validateTableSelect(String columns, String rows) {
    // 연속된 콤마 체크 (e.g., "1,,2" or ",1" or "1,")
    if (columns.contains(SEPARATOR + SEPARATOR) || columns.startsWith(SEPARATOR) || columns.endsWith(SEPARATOR)) {
      return false;
    }

    String[] selections = columns.split(SEPARATOR);
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
