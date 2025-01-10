package api.mcnc.surveynotificationservice.entity;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static api.mcnc.surveynotificationservice.constants.RedirectPrefix.REDIRECT_URI_PREFIX;

/**
 * 알림 엔티티 클래스.
 * @author 차익현
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;  // UUID로 생성

    @Column(nullable = false)
    private String adminId;  // 관리자 ID

    @Column(nullable = false)
    private String surveyId;  // 설문 ID

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;  // 알림 타입 (예: 설문 시작, 종료 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;  // 알림 상태 (읽음/읽지 않음)

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 알림 생성 시간

    @Transient
    public String getRedirectUrl() {
        return REDIRECT_URI_PREFIX + surveyId;
    }

    // Builder 패턴을 사용하여 객체 생성
    @Builder
    public NotificationEntity(String adminId, String surveyId, String message, Type type, Status status) {
        this.adminId = adminId;
        this.surveyId = surveyId;
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public static NotificationEntity of(String adminId, String surveyId, String message, Type type) {
        return NotificationEntity.builder()
                .adminId(adminId)
                .surveyId(surveyId)
                .message(message)
                .type(type)
                .status(Status.UNREAD)
                .build();
    }

    public NotificationDto toDto() {
        return NotificationDto.fromEntity(this);
    }

    // 읽음 처리
    public void setRead() {
        this.status = Status.READ;
    }

    // 알림 타입 (예: 설문 시작, 설문 종료 등)
    public enum Type {
        SURVEY_START,
        SURVEY_END,
        SURVEY_CREATE,
        SURVEY_EDIT,
        SURVEY_DELETE
    }

    // 알림 상태 (읽음 / 읽지 않음)
    public enum Status {
        READ,
        UNREAD
    }
}
