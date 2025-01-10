package api.mcnc.surveynotificationservice.repository;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 알림 데이터를 관리하는 리포지토리
 * @author 차익현
 */
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // 특정 관리자의 미확인 알림 개수 조회
    long countByAdminIdAndStatus(String adminId, NotificationEntity.Status status);

    // 특정 관리자의 모든 알림 조회
    List<NotificationEntity> findByAdminIdOrderByCreatedAtDesc(String adminId, Pageable pageable);

    // 알림 삭제
    void deleteByIdAndAdminId(Long id, String adminId);

    // 특정 알림을 관리자 ID로 조회
    Optional<NotificationEntity> findByIdAndAdminId(Long id, String adminId);
}
