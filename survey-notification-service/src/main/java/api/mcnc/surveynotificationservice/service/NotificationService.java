package api.mcnc.surveynotificationservice.service;
import api.mcnc.surveynotificationservice.dto.Request;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
//import api.mcnc.surveynotificationservice.entity.SurveyEntity;
import api.mcnc.surveynotificationservice.repository.NotificationRepository;
//import api.mcnc.surveynotificationservice.repository.SurveyRepository;
//import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
//    private final SurveyRepository surveyRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
//        this.surveyRepository = surveyRepository;  (SurveyRepository surveyRepository)
    }

    // 미확인된 알림의 개수 조회
    public long countUnreadNotifications(String adminId) {
        return notificationRepository.countByAdminIdAndStatus(adminId, NotificationEntity.Status.UNREAD);
    }


    // 알림 상태를 무조건 READ로 업데이트
    public void updateNotificationStatus(String notificationId) {
        notificationRepository.updateStatusToReadById(notificationId);
    }



    public List<NotificationEntity> getAllNotificationsByAdmin(String adminId) {
        return notificationRepository.findByAdminId(adminId);
    }

    public void deleteNotificationById(String id) {
        notificationRepository.deleteById(id);
    }

    public void createNotifications(List<Request> requests) {
        for (Request request : requests) {
            boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
            if (!exists) {
                NotificationEntity notification = NotificationEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .surveyId(request.surveyId())
                        .adminId(request.adminId())
                        .type(request.type())
                        .status(NotificationEntity.Status.UNREAD)
                        .createdAt(LocalDateTime.now())
                        .build();
                notificationRepository.save(notification);
            }
        }
    }


    public void createNotification(Request request) {
        boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
        if (!exists) {
            NotificationEntity notification = NotificationEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .surveyId(request.surveyId())
                    .adminId(request.adminId())
                    .type(request.type())
                    .status(NotificationEntity.Status.UNREAD)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);
        }
    }

}

