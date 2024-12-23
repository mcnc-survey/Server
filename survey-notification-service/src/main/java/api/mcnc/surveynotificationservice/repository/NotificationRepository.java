package api.mcnc.surveynotificationservice.repository;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // 특정 관리자의 미확인 알림 개수 조회
    long countByAdminIdAndStatus(String adminId, NotificationEntity.Status status);

    // 특정 관리자의 모든 알림 조회
    List<NotificationEntity> findByAdminIdOrderByCreatedAtDesc(String adminId, Pageable pageable);


    void deleteByIdAndAdminId(Long id, String adminId);

    Optional<NotificationEntity> findByIdAndAdminId(Long id, String adminId);
}
