package api.mcnc.survey.common.exception;

import api.mcnc.survey.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public abstract class GlobalException extends RuntimeException{
    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
    }
}
