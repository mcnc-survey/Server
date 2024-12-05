package api.mcnc.surveynotificationservice.repository;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    // 미확인된 알림의 개수를 셈
    long countByAdminIdAndStatus(String adminId, NotificationEntity.Status status);

    // 알림 상태를 변경하는 메소드 예시
    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status WHERE n.id = :id")
    void updateStatusById(String id, NotificationEntity.Status status);

    List<NotificationEntity> findByAdminId(String adminId);

    void deleteBySurveyId(String surveyId);


    // 설문 ID와 알림 타입을 기준으로 중복 알림이 있는지 확인
    boolean existsBySurveyIdAndType(String surveyId, NotificationEntity.Type type);

}