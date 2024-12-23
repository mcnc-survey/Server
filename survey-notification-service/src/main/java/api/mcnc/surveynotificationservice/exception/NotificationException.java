package api.mcnc.surveynotificationservice.exception;

import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {

    private final String errorCode;

    public NotificationException(String errorCode) {
        super(errorCode);  // 부모 클래스의 메시지로 전달
        this.errorCode = errorCode;
    }

}
