package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static api.mcnc.surveynotificationservice.entity.NotificationEntity.Status.READ;

@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NotificationDto {

    private Long id;
    private String message;
    private Boolean isRead;
    private String createdAt;
    private NotificationEntity.Type type;

    public static NotificationDto fromEntity(NotificationEntity notificationEntity) {
        return NotificationDto.builder()
                .id(notificationEntity.getId())
                .message(notificationEntity.getMessage())
                .isRead(READ.equals(notificationEntity.getStatus()))
                .createdAt(notificationEntity.getCreatedAt().toString())
                .type(notificationEntity.getType())
                .build();
    }
}
