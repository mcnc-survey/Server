package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NotificationDto {

    private String id;
    private String surveyId;
    private String adminId;
    private String type;
    private String status;
    private String createdAt;

    public static NotificationDto fromEntity(NotificationEntity notificationEntity) {
        return new NotificationDto(
                notificationEntity.getId(),
                notificationEntity.getSurveyId(),
                notificationEntity.getAdminId(),
                notificationEntity.getType().name(),
                notificationEntity.getStatus().name(),
                notificationEntity.getCreatedAt().toString()
        );
    }
}
