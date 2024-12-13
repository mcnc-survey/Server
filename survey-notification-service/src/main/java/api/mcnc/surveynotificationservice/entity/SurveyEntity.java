//package api.mcnc.surveynotificationservice.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "surveys")
//public class SurveyEntity {
//
//    @Id
//    @Column(name = "id", columnDefinition = "CHAR(36)")
//    private String id;
//
//    @Column(name = "admin_id")
//    private String adminId;
//
//    @Column(name = "title")
//    private String title;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    @Column(name = "end_at")
//    private LocalDateTime endAt;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    public enum Status {
//        ACTIVE, INACTIVE, COMPLETED;
//    }
//
//    // 기본 생성자
//    public SurveyEntity() {}
//
//    // 생성자, 게터/세터
//    public SurveyEntity(String id, String adminId, String title, String description, Status status, LocalDateTime endAt, LocalDateTime createdAt) {
//        this.id = id;
//        this.adminId = adminId;
//        this.title = title;
//        this.description = description;
//        this.status = status;
//        this.endAt = endAt;
//        this.createdAt = createdAt;
//    }
//
//    // Getter와 Setter
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getAdminId() {
//        return adminId;
//    }
//
//    public void setAdminId(String adminId) {
//        this.adminId = adminId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public LocalDateTime getEndAt() {
//        return endAt;
//    }
//
//    public void setEndAt(LocalDateTime endAt) {
//        this.endAt = endAt;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//}
