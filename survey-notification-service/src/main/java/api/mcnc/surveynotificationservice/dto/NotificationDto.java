package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static api.mcnc.surveynotificationservice.entity.NotificationEntity.Status.READ;

/**
 * 알림 정보를 전달하기 위한 DTO 클래스
 * @author 차익현
 */
@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class NotificationDto {

    private Long id;
    private String surveyId;
    private String message;
    private Boolean isRead;
    private String createdAt;
    private NotificationEntity.Type type;

    public static NotificationDto fromEntity(NotificationEntity notificationEntity) {
        return NotificationDto.builder()
                .id(notificationEntity.getId())
                .surveyId(notificationEntity.getSurveyId())
                .message(notificationEntity.getMessage())
                .isRead(READ.equals(notificationEntity.getStatus()))
                .createdAt(notificationEntity.getCreatedAt().toString())
                .type(notificationEntity.getType())
                .build();
    }

    public static NotificationDto of(Long id, String surveyId, String message, Boolean isRead, String createdAt, NotificationEntity.Type type) {
        return NotificationDto.builder()
                .id(id)
                .surveyId(surveyId)
                .message(message)
                .isRead(isRead)
                .createdAt(createdAt)
                .type(type)
                .build();
    }
}
