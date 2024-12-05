package api.mcnc.surveynotificationservice.controller;

import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public void updateNotificationStatus(@PathVariable String id, @RequestParam NotificationEntity.Status status) {
        notificationService.updateNotificationStatus(id, status);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<NotificationEntity>> getAllNotificationsByAdmin(@RequestParam String adminId) {
        List<NotificationEntity> notifications = notificationService.getAllNotificationsByAdmin(adminId);
        return ResponseEntity.ok(notifications);
    }





//    // 설문 마감 알림 생성 트리거(내가 한거아님)
//    @PostMapping("/generate-endline")
//    public ResponseEntity<String> createEndlineNotifications() {
//        notificationService.createEndlineNotifications();
//        return ResponseEntity.ok("Endline notifications generated successfully");
//    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createNotifications() {
//        notificationService.processNotifications();
//        return ResponseEntity.ok("Notifications created successfully.");
//    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.noContent().build();
    }

    // 다른데서 이거 알림으로 만들어줘 요청
    @SneakyThrows(JsonProcessingException.class)
    @PostMapping("/create")
    public ResponseEntity<String> createNotifications(String info){
        String i = new String(Base64.decode(info));
        Request obj = objectMapper.readValue(i, Request.class);

//        notificationService.processNotifications();
        return ResponseEntity.ok("Notifications created successfully.");
    }

    public record Request  (
        String adminId,
        String surveyId,
        NotificationEntity.Type type
    ) {

    }
}
