package api.mcnc.surveynotificationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Table(name = "notifications")
@Getter
@Setter
public class NotificationEntity {

    @Id
    @Column(name = "id", length = 40)
    private String id;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "survey_id")
    private String surveyId;


    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        SURVEY_END,SURVEY_ENDING_SOON;
    }


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        READ, UNREAD;
    }

    // 기본 생성자
    public NotificationEntity() {
    }

    // 모든 필드를 포함한 생성자
    public NotificationEntity(String id, String adminId, String surveyId, Type type, LocalDateTime createdAt, Status status) {
        this.id = id;
        this.adminId = adminId;
        this.surveyId = surveyId;
        this.type = type;
        this.createdAt = createdAt;
        this.status = status;
    }



//    public static NotificationEntity from(SurveyEntity survey) {
//        return NotificationEntity.builder()
//                .id(UUID.randomUUID().toString())
//                .surveyId(survey.getId())
//                .adminId(survey.getAdminId())
//                .type(Type.endline)
//                .createdAt(LocalDateTime.now())
//                .status(Status.UNREAD)
//                .build();
//    }

}



