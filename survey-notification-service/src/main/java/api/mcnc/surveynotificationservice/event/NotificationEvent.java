package api.mcnc.surveynotificationservice.event;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import lombok.Builder;

/**
 * 알림 이벤트 클래스
 * @author 차익현
 * */
@Builder
public record NotificationEvent(
        String adminId,
        String surveyId,
        String message,
        NotificationEntity.Type type
) {
}