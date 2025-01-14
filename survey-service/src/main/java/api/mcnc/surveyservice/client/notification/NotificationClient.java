package api.mcnc.surveyservice.client.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-20 오후 12:49
 */
@FeignClient(name = "survey-notification-service")
public interface NotificationClient {
  @PostMapping("/notifications/publish")
  ResponseEntity<Void> publishNotificationToRedis(@RequestBody Request request, @RequestHeader(value = "requested-by") String adminId);
}
