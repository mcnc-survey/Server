package api.mcnc.surveynotificationservice.event;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import lombok.Builder;

@Builder
public record NotificationEvent(
        String adminId,
        String surveyId,
        String message,
        NotificationEntity.Type type
) {
}