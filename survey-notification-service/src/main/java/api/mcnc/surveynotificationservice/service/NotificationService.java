package api.mcnc.surveynotificationservice.service;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.entity.SurveyEntity;
import api.mcnc.surveynotificationservice.repository.NotificationRepository;
import api.mcnc.surveynotificationservice.repository.SurveyRepository;
import jakarta.transaction.Transactional;
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
    private final SurveyRepository surveyRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SurveyRepository surveyRepository) {
        this.notificationRepository = notificationRepository;
        this.surveyRepository = surveyRepository;
    }

    // 미확인된 알림의 개수 조회
    public long countUnreadNotifications(String adminId) {
        return notificationRepository.countByAdminIdAndStatus(adminId, NotificationEntity.Status.UNREAD);
    }


    // 알림 상태 업데이트
    public void updateNotificationStatus(String notificationId, NotificationEntity.Status status) {
        notificationRepository.updateStatusById(notificationId, status);
    }


    public List<NotificationEntity> getAllNotificationsByAdmin(String adminId) {
        return notificationRepository.findByAdminId(adminId);
    }

    public void deleteNotificationById(String id) {
        notificationRepository.deleteById(id);
    }


//    //내가 한거아님
//    @Transactional
//    public void createEndlineNotifications() {
//        List<SurveyEntity> surveys = surveyRepository.findSurveysEndingSoon(LocalDateTime.now());
//        surveys
//            .stream()
//            .map(NotificationEntity::from)
//            .forEach(notificationRepository::save);
//        //그래서 이게 뭔데!!!!!!!!!!피곤하다 존나게 말이지...우야노
//    }



    public void processNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
//
//        // 1. 설문 종료 알림
//        List<SurveyEntity> endedSurveys = surveyRepository.findSurveysCompleted(now);
//        createNotifications(endedSurveys, NotificationEntity.Type.valueOf("SURVEY_END"));

        // 2. 마감 30분 전 알림
        List<SurveyEntity> surveysEndingSoon = surveyRepository.findSurveysEndingSoon(now, thirtyMinutesLater);
        createNotifications(surveysEndingSoon, NotificationEntity.Type.valueOf("SURVEY_ENDING_SOON"));

//        // 3. 100명 응답 달성 알림 (response_count 컬럼 추가 후)
//        List<SurveyEntity> surveysWith100Responses = surveyRepository.findSurveysWith100Responses();
//        createNotifications(surveysWith100Responses, "SURVEY_100_RESPONSES");
    }

    private void createNotifications(List<SurveyEntity> surveys, NotificationEntity.Type type) {
        for (SurveyEntity survey : surveys) {
            // 이미 같은 설문과 알림 타입에 대한 알림이 존재하는지 확인
            boolean exists = notificationRepository.existsBySurveyIdAndType(survey.getId(), type);
            System.out.println(exists);

            // 알림이 없으면 새로 생성
            if (!exists) {
                // NotificationEntity를 from 메서드를 통해 생성
                NotificationEntity notification = NotificationEntity.builder()
                        .id(UUID.randomUUID().toString())               // 새로운 ID 생성
                        .surveyId(survey.getId())                       // 설문 ID
                        .adminId(survey.getAdminId())                   // 관리자 ID
                        .type(type)                                     // Type은 Enum으로 처리
                        .status(NotificationEntity.Status.UNREAD)      // 상태는 UNREAD로 기본 설정
                        .createdAt(LocalDateTime.now())                 // 현재 시간으로 생성 일자 설정
                        .build();

                // 알림 저장
                System.out.println("Saving notification: " + notification);
                notificationRepository.save(notification);
                System.out.println("Notification saved successfully.");

            }
        }
    }

}

