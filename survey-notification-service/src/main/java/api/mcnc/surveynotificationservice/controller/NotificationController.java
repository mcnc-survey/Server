package api.mcnc.surveynotificationservice.controller;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.dto.Request;
import api.mcnc.surveynotificationservice.event.NotificationEventPublisher;
import api.mcnc.surveynotificationservice.exception.NotificationException;
import api.mcnc.surveynotificationservice.service.NotificationService;
import api.mcnc.surveynotificationservice.service.RedisMessageService;
import api.mcnc.surveynotificationservice.service.SseEmitterService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;
    private final NotificationEventPublisher publisher;

    // 미확인 알림 개수 조회
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount(@RequestHeader(value = "requested-by") String adminId) {
        long unreadCount = notificationService.countUnreadNotifications(adminId);
        return ResponseEntity.ok(unreadCount);
    }

    // 알림 상태를 'READ'로 업데이트
    @PatchMapping("/read/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id, @RequestHeader(value = "requested-by") String adminId) {
        URI redirectUri = notificationService.redirectNotificationDetail(id, adminId);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(redirectUri)
                .build();
    }

    // 관리자의 모든 알림 조회
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@RequestHeader(value = "requested-by") String adminId) {
        List<NotificationDto> notifications = notificationService.getAllNotificationsByAdmin(adminId);
        return ResponseEntity.ok(notifications);
    }

    // 알림 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, @RequestHeader(value = "requested-by") String adminId) {
        notificationService.deleteNotificationById(id, adminId);
        return ResponseEntity.noContent().build();
    }

    /**
     * SSE 구독
     *
     * @param adminId 헤더로 전달된 관리자 ID
     * @return SseEmitter 객체
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @RequestHeader(value = "requested-by") String adminId) {
        if (adminId == null || adminId.isEmpty()) {
            throw new NotificationException("요청정보를 찾을 수 없습니다.");  // ErrorCode 전달
        }

        SseEmitter emitter = notificationService.subscribe(adminId);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(emitter);
    }

    // SSE 구독 해제
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribeFromNotifications(@RequestHeader(value = "requested-by") String adminId) {
        sseEmitterService.deleteEmitter(adminId);
        redisMessageService.removeSubscribe(adminId);
        return ResponseEntity.noContent().build();
    }

    // Redis 메시지로 알림 전송 (옵션, 필요에 따라 사용)
    @PostMapping("/publish")
    public ResponseEntity<Void> publishNotificationToRedis(@RequestBody Request request, @RequestHeader(value = "requested-by") String adminId) {
        publisher.publishEvent(request.toEvent(adminId));
        return ResponseEntity.status(202).build();
    }
}
