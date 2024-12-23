package api.mcnc.surveynotificationservice.service;

import api.mcnc.surveynotificationservice.constants.MsgFormat;
import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.event.NotificationEvent;
import api.mcnc.surveynotificationservice.exception.NotificationException;
import api.mcnc.surveynotificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RedisMessageService redisMessageService;
    private final SseEmitterService sseEmitterService;

    /**
     * 미확인된 알림의 개수 조회
     *
     * @param adminId 관리자의 ID
     * @return 미확인 알림의 개수
     */
    public long countUnreadNotifications(String adminId) {
        return notificationRepository.countByAdminIdAndStatus(adminId, NotificationEntity.Status.UNREAD);
    }

    /**
     * 알림 상태를 'READ'로 업데이트
     *
     * @param id 알림 ID
     */
    @Transactional
    public URI redirectNotificationDetail(Long id, String adminId) {
        NotificationEntity notificationEntity = notificationRepository.findByIdAndAdminId(id, adminId).orElseThrow(()-> new NotificationException("요청정보를 찾을 수 없습니다."));
        notificationEntity.setRead();
        return URI.create(notificationEntity.getRedirectUrl());
    }

    /**
     * 관리자의 모든 알림 조회
     *
     * @param adminId 관리자의 ID
     * @return 알림 목록
     */
    public List<NotificationDto> getAllNotificationsByAdmin(String adminId) {
        Pageable pageable = PageRequest.of(0, 30);
        return notificationRepository
                .findByAdminIdOrderByCreatedAtDesc(adminId, pageable)
                .stream()
                .map(NotificationEntity::toDto)
                .toList();
    }

    /**
     * 알림 삭제
     *
     * @param id 알림 ID
     */
    public void deleteNotificationById(Long id, String adminId) {
        notificationRepository.deleteByIdAndAdminId(id, adminId);
    }

    @Transactional
    public void sendNotification(NotificationEvent event) {
        NotificationEntity notification =
                NotificationEntity.of(event.adminId(), event.surveyId(), event.message(), event.type());
        NotificationEntity savedNotification = notificationRepository.save(notification);

        redisMessageService.publish(event.adminId(), NotificationDto.fromEntity(savedNotification));
    }

    public SseEmitter subscribe(String adminId) {
        SseEmitter sseEmitter = sseEmitterService.createEmitter(adminId);
        sseEmitterService.send(MsgFormat.SUBSCRIBE, adminId, sseEmitter); // send dummy

        redisMessageService.subscribe(adminId);

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError((e) -> sseEmitter.complete());
        sseEmitter.onCompletion(() -> {
            sseEmitterService.deleteEmitter(adminId);
            redisMessageService.removeSubscribe(adminId);
        });
        return sseEmitter;
    }
}
