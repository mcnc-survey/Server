//package api.mcnc.surveynotificationservice.controller;
//
//import api.mcnc.surveynotificationservice.dto.Request;
//import api.mcnc.surveynotificationservice.entity.NotificationEntity;
//import api.mcnc.surveynotificationservice.service.NotificationService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.bouncycastle.util.encoders.Base64;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/notifications")
//@RequiredArgsConstructor
//public class NotificationController {
//
//    private final ObjectMapper objectMapper;
//
//    private final NotificationService notificationService;
//
//    // 미확인 알림 개수 조회 API
//    @GetMapping("/count-unread")
//    public ResponseEntity<Long> countUnreadNotifications(@RequestParam String adminId) {
//        long unreadCount = notificationService.countUnreadNotifications(adminId);
//        return ResponseEntity.ok(unreadCount);
//    }
//
//    // 알림 상태 업데이트 API
//    @PatchMapping("/{id}/status")
//    public void updateNotificationStatus(@PathVariable String id) {
//        notificationService.updateNotificationStatus(id);
//    }
//
//
//    // 모든 알림 조회
//    @GetMapping("/findAll")
//    public ResponseEntity<List<NotificationEntity>> getAllNotificationsByAdmin(@RequestParam String adminId) {
//        List<NotificationEntity> notifications = notificationService.getAllNotificationsByAdmin(adminId);
//        return ResponseEntity.ok(notifications);
//    }
//
//    // 알림 삭제
//    @DeleteMapping("/{id}/delete")
//    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
//        notificationService.deleteNotificationById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//
//    //여러 개
//    @SneakyThrows
//    @PostMapping("/create-bulk")
//    public ResponseEntity<String> createBulkNotifications(@RequestBody String info) {
//        try {
//            String decoded = new String(Base64.decode(info));
//            List<Request> requests = objectMapper.readValue(decoded, new TypeReference<List<Request>>() {});
//            notificationService.createNotifications(requests);
//            return ResponseEntity.ok("Notifications created successfully for all requests.");
//        } catch (Exception e) {
//            e.printStackTrace(); // 로그로 남기기
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//        }
//    }
//
//
//
//    //한 개
//    @SneakyThrows(JsonProcessingException.class)
//    @PostMapping("/create")
//    public ResponseEntity<String> createNotification(@RequestBody Request request) {
//
//        // Service 호출
//        notificationService.createNotification(request);
//
//        return ResponseEntity.ok("Notification created successfully.");
//    }
//}
package api.mcnc.surveynotificationservice.controller;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.dto.Request;
import api.mcnc.surveynotificationservice.exception.ErrorCode;
import api.mcnc.surveynotificationservice.exception.NotificationException;
import api.mcnc.surveynotificationservice.service.NotificationService;
import api.mcnc.surveynotificationservice.service.SseEmitterService;
import api.mcnc.surveynotificationservice.service.RedisMessageService;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;

    public NotificationController(NotificationService notificationService,
                                  SseEmitterService sseEmitterService,
                                  RedisMessageService redisMessageService) {
        this.notificationService = notificationService;
        this.sseEmitterService = sseEmitterService;
        this.redisMessageService = redisMessageService;
    }

    // 미확인 알림 개수 조회
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount(@RequestParam String adminId) {
        long unreadCount = notificationService.countUnreadNotifications(adminId);
        return ResponseEntity.ok(unreadCount);
    }

    // 알림 상태를 'READ'로 업데이트
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String id) {
        notificationService.updateNotificationStatus(id);
        return ResponseEntity.noContent().build();
    }

    // 관리자의 모든 알림 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<NotificationEntity>> getAllNotifications(@RequestParam String adminId) {
        List<NotificationEntity> notifications = notificationService.getAllNotificationsByAdmin(adminId);
        return ResponseEntity.ok(notifications);
    }

    // 알림 삭제
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.noContent().build();
    }

//    // 여러 개 알림 생성
//    @PostMapping("/bulk")
//    public ResponseEntity<Void> createMultipleNotifications(@RequestBody List<Request> requests) {
//        notificationService.createNotifications(requests);
//        return ResponseEntity.status(201).build();
//    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> createBulkNotifications(@RequestBody String base64EncodedInfo) {
        try {
            notificationService.createNotifications(base64EncodedInfo);
            return ResponseEntity.status(201).build();
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
            return ResponseEntity.status(500).build();
        }
    }

    // 알림 한 개 생성
    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody Request request) {
        notificationService.createNotification(request);
        return ResponseEntity.status(201).build();
    }

//    // SSE로 알림 구독
////    @GetMapping(value ="/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @GetMapping(value ="/subscribe")
//    public SseEmitter subscribeToNotifications(@RequestParam String adminId) {
//        SseEmitter sseEmitter = sseEmitterService.createEmitter(adminId);
//        redisMessageService.subscribe(adminId);
//        return sseEmitter;
//    }
    /**
     * SSE 구독
     *
     * @param adminId 헤더로 전달된 관리자 ID
     * @return SseEmitter 객체
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @RequestHeader(value = "X-Admin-ID", required = false) String adminId) {
        if (adminId == null || adminId.isEmpty()) {
            throw new NotificationException("꺼져안되니까");  // ErrorCode 전달
        }

        SseEmitter emitter = notificationService.subscribe(adminId);
        return ResponseEntity.ok(emitter);
    }



    // SSE 구독 해제
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribeFromNotifications(@RequestParam String adminId) {
        sseEmitterService.deleteEmitter(adminId);
        redisMessageService.removeSubscribe(adminId);
        return ResponseEntity.noContent().build();
    }

    // Redis 메시지로 알림 전송 (옵션, 필요에 따라 사용)
    @PostMapping("/publish")
    public ResponseEntity<Void> publishNotificationToRedis(@RequestBody NotificationDto notificationDto) {
        redisMessageService.publish(notificationDto.getAdminId(), notificationDto);
        return ResponseEntity.status(202).build();
    }
}
