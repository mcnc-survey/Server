package api.mcnc.surveyrespondentservice.common.exception;

import api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode;
import api.mcnc.surveyrespondentservice.common.exception.custom.RespondentException;
import api.mcnc.surveyrespondentservice.common.result.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-14 오후 9:37
 */
@RestControllerAdvice
public class ResponseExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Api<String>> handleRuntimeException(RuntimeException e) {
    Api<String> response = Api.fail(RespondentErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    e.printStackTrace();
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(RespondentException.class)
  public ResponseEntity<Api<String>> handleResponseException(RespondentException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Api<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<ErrorField> errorList = e.getBindingResult()
      .getAllErrors()
      .stream()
      .map(ErrorField::toErrorField)
      .toList();
    Api<Object> response = Api.fail(RespondentErrorCode.INVALID_REQUEST, errorList);
    return ResponseEntity.ok(response);
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
