package api.mcnc.survey.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminExceptionCode {
    ADMIN_NOT_FOUND("ADM0001", "존재하지 않는 계정입니다."),

    ;
    private final String code;
    private final String message;
}
