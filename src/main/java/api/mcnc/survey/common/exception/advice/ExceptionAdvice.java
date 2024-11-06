package api.mcnc.survey.common.exception.advice;

import api.mcnc.survey.common.exception.GlobalException;
import api.mcnc.survey.common.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleGlobalException(GlobalException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.info(errorCode.getMessage());
        return ResponseEntity.ok(errorCode.getCode());
    }
}
