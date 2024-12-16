package api.mcnc.surveynotificationservice.controller;

import api.mcnc.surveynotificationservice.dto.Request;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final ObjectMapper objectMapper;

    private final NotificationService notificationService;

    // 미확인 알림 개수 조회 API
    @GetMapping("/count-unread")
    public ResponseEntity<Long> countUnreadNotifications(@RequestParam String adminId) {
        long unreadCount = notificationService.countUnreadNotifications(adminId);
        return ResponseEntity.ok(unreadCount);
    }

    // 알림 상태 업데이트 API
    @PatchMapping("/{id}/status")
    public void updateNotificationStatus(@PathVariable String id) {
        notificationService.updateNotificationStatus(id);
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<NotificationEntity>> getAllNotificationsByAdmin(@RequestParam String adminId) {
        List<NotificationEntity> notifications = notificationService.getAllNotificationsByAdmin(adminId);
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.noContent().build();
    }


//    @SneakyThrows(JsonProcessingException.class)
//    @PostMapping("/create-bulk")
//    public ResponseEntity<String> createBulkNotifications(@RequestBody String info) {
//        // Base64 디코딩
//        String decoded = new String(Base64.decode(info));
//
//        // JSON 문자열을 List<Request>로 변환
//        List<Request> requests = objectMapper.readValue(decoded, new TypeReference<List<Request>>() {});
//
//        // Service 호출
//        notificationService.createNotifications(requests);
//
//        return ResponseEntity.ok("Notifications created successfully for all requests.");
//    }

    @SneakyThrows
    @PostMapping("/create-bulk")
    public ResponseEntity<String> createBulkNotifications(@RequestBody String info) {
        try {
            String decoded = new String(Base64.decode(info));
            List<Request> requests = objectMapper.readValue(decoded, new TypeReference<List<Request>>() {});
            notificationService.createNotifications(requests);
            return ResponseEntity.ok("Notifications created successfully for all requests.");
        } catch (Exception e) {
            e.printStackTrace(); // 로그로 남기기
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



    @SneakyThrows(JsonProcessingException.class)
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody String info) {
        // Base64 디코딩
        String decoded = new String(Base64.decode(info));

        // JSON 문자열을 Request 객체로 변환
        Request obj = objectMapper.readValue(decoded, Request.class);

        // Service 호출
        notificationService.createNotification(obj);

        return ResponseEntity.ok("Notification created successfully.");
    }
}
