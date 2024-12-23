package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.event.NotificationEvent;

public record Request(
        String surveyId,
        String message,
        NotificationEntity.Type type
) {
    public NotificationEvent toEvent(String adminId) {
        return NotificationEvent.builder()
                .adminId(adminId)
                .surveyId(surveyId)
                .message(message)
                .type(type)
                .build();
    }
}
