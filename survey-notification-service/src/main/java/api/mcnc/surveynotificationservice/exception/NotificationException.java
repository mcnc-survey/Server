package api.mcnc.surveynotificationservice.exception;

import lombok.Getter;

/**
 * 알림 관련 예외 클래스.
 * @author 차익현
 * */
@Getter
public class NotificationException extends RuntimeException {

    private final String errorCode;

    public NotificationException(String errorCode) {
        super(errorCode);  // 부모 클래스의 메시지로 전달
        this.errorCode = errorCode;
    }

}
