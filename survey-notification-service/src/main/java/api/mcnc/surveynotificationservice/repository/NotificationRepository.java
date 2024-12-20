//package api.mcnc.surveynotificationservice.repository;
//
//import api.mcnc.surveynotificationservice.entity.NotificationEntity;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
//
//    // 미확인된 알림의 개수를 셈
//    long countByAdminIdAndStatus(String adminId, NotificationEntity.Status status);
//
//    // 알림 상태를 변경하는 메소드 예시
//    @Transactional
//    @Modifying
//    @Query("UPDATE NotificationEntity n SET n.status = 'READ' WHERE n.id = :id")
//    void updateStatusToReadById(String id);
//
//
//    List<NotificationEntity> findByAdminId(String adminId);
//
//    // 설문 ID와 알림 타입을 기준으로 중복 알림이 있는지 확인
//    boolean existsBySurveyIdAndType(String surveyId, NotificationEntity.Type type);
//
//}
package api.mcnc.surveynotificationservice.repository;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

    // 특정 관리자의 미확인 알림 개수 조회
    long countByAdminIdAndStatus(String adminId, NotificationEntity.Status status);

    // 알림 상태를 'READ'로 업데이트
    //알림 상태를 변경하는 메소드 예시
    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ' WHERE n.id = :id")
    void updateStatusToReadById(String id);

    // 특정 관리자의 모든 알림 조회
    List<NotificationEntity> findByAdminId(String adminId);

    // 설문 ID와 알림 유형에 해당하는 알림이 이미 존재하는지 확인
    boolean existsBySurveyIdAndType(String surveyId, NotificationEntity.Type type);

    // 알림 ID로 알림 조회
    Optional<NotificationEntity> findById(String id);

    // 알림 삭제
    void deleteById(String id);
}
