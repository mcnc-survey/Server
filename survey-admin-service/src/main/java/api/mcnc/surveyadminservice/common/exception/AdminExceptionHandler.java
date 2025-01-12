package api.mcnc.surveyadminservice.common.exception;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.result.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * exception handler
 *
 * @author 유희준
 * @since :2024-11-26 오전 10:30
 */
@RestControllerAdvice
public class AdminExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Api<String>> handleRuntimeException(RuntimeException e) {
    // 임시 메시지
    // TODO : 에러 메시지 정의 필요
    Api<String> response = Api.fail(AdminErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
//    e.printStackTrace();
    return ResponseEntity.internalServerError().body(response);
  }

  @ExceptionHandler(AdminException.class)
  public ResponseEntity<Api<String>> handleResponseException(AdminException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(TokenException.class)
  public ResponseEntity<Api<String>> handleTokenException(TokenException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Api<String>> handleAuthException(AuthException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(VaultException.class)
  public ResponseEntity<Api<String>> handleVaultException(VaultException e) {
    Api<String> response = Api.fail(e.getCode(), e.getMessage());
    return ResponseEntity.internalServerError().body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Api<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<ErrorField> errorList = e.getBindingResult()
      .getAllErrors()
      .stream()
      .map(ErrorField::toErrorField)
      .toList();
    Api<Object> response = Api.fail(AdminErrorCode.INVALID_REQUEST, errorList);
    return ResponseEntity.badRequest().body(response);
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
