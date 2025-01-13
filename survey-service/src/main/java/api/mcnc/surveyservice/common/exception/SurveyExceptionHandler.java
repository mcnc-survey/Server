package api.mcnc.surveyservice.common.exception;

import api.mcnc.surveyservice.common.enums.SurveyErrorCode;
import api.mcnc.surveyservice.common.exception.custom.QuestionException;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.common.result.Api;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-14 오후 9:37
 */
@RestControllerAdvice
public class SurveyExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Api<String>> handleRuntimeException(RuntimeException e) {
    Api<String> response = Api.fail(SurveyErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    e.printStackTrace();
    return ResponseEntity.ok(response);
  }
// TODO 2024-12-19 yhj : 상황에 맞는 status 내려 주는 것이 나을 듯
  @ExceptionHandler(SurveyException.class)
  public ResponseEntity<Api<String>> handleResponseException(SurveyException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(QuestionException.class)
  public ResponseEntity<Api<String>> handleQuestionException(QuestionException e) {
    Api<String> response = Api.fail(e.getCode(), null);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Api<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<ErrorField> errorList = e.getBindingResult()
      .getAllErrors()
      .stream()
      .map(ErrorField::toErrorField)
      .toList();
    Api<Object> response = Api.fail(SurveyErrorCode.INVALID_REQUEST, errorList);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Api<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.ok(Api.fail(SurveyErrorCode.INVALID_JSON_REQUEST, null));
  }


// validation error field
  private record ErrorField(String field, String value) {
    static ErrorField toErrorField(ObjectError error) {
      FieldError fieldError = (FieldError) error;
      String field = fieldError.getField();
      String message = Objects.requireNonNull(error.getDefaultMessage());
      return new ErrorField(field, message);
    }
  }
}
