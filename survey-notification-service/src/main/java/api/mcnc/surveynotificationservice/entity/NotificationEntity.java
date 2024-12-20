//package api.mcnc.surveynotificationservice.entity;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Builder
//@Table(name = "notifications")
//@Getter
//@Setter
//public class NotificationEntity {
//
//    @Id
//    @Column(name = "id", length = 40)
//    private String id;
//
//    @Column(name = "admin_id")
//    private String adminId;
//
//    @Column(name = "survey_id")
//    private String surveyId;
//
//
//    @Column(name = "type")
//    @Enumerated(EnumType.STRING)
//    private Type type;
//
//    public enum Type {
//        SURVEY_END, SURVEY_START, SURVEY_CREATE, SURVEY_EDIT, SURVEY_DELETE;
//    }
//
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    public enum Status {
//        READ, UNREAD;
//    }
//
//    // 기본 생성자
//    public NotificationEntity() {
//    }
//
//    // 모든 필드를 포함한 생성자
//    public NotificationEntity(String id, String adminId, String surveyId, Type type, LocalDateTime createdAt, Status status) {
//        this.id = id;
//        this.adminId = adminId;
//        this.surveyId = surveyId;
//        this.type = type;
//        this.createdAt = createdAt;
//        this.status = status;
//    }
//
//
//}
//
//
//
package api.mcnc.surveynotificationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationEntity {

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 알림 생성 시간

    @Id
    @Column(name = "id", length = 40)
    private String id;  // UUID로 생성

    @Column(nullable = false)
    private String adminId;  // 관리자 ID

    @Column(nullable = false)
    private String surveyId;  // 설문 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;  // 알림 타입 (예: 설문 시작, 종료 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;  // 알림 상태 (읽음/읽지 않음)


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

//    // 모든 필드를 포함한 생성자
//    public NotificationEntity(String id, String adminId, String surveyId, Type type, Status status, LocalDateTime createdAt) {
//        this.id = id;
//        this.adminId = adminId;
//        this.surveyId = surveyId;
//        this.type = type;
//        this.status = status;
//        this.createdAt = createdAt;
//    }

    // Builder 패턴을 사용하여 객체 생성
    @Builder
    public NotificationEntity(LocalDateTime createdAt, String id, String adminId, String surveyId, Type type, Status status) {
        this.createdAt = createdAt;
        this.id = UUID.randomUUID().toString();  // id는 UUID로 자동 생성
        this.adminId = adminId;
        this.surveyId = surveyId;
        this.type = type;
        this.status = status;
    }

    // 읽음 처리
    public void setRead() {
        this.status = Status.READ;
    }
}
