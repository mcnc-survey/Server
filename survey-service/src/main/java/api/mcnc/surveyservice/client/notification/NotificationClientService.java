package api.mcnc.surveyservice.client.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-20 오후 12:49
 */
@Service
@RequiredArgsConstructor
public class NotificationClientService {

  private final NotificationClient notificationClient;

  @Async
  public void publishNotification(Request request, String adminId) {
    notificationClient.publishNotificationToRedis(request, adminId);
  }
}
