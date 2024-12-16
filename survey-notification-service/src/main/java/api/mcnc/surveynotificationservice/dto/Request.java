package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;

public record Request(
        String adminId,
        String surveyId,
        NotificationEntity.Type type
) {}
