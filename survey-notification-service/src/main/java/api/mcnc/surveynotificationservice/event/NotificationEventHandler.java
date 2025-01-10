package api.mcnc.surveynotificationservice.event;

import api.mcnc.surveynotificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 알림 이벤트를 처리하는 핸들러.
 * @author 차익현
 */
@RequiredArgsConstructor
@Component
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleEvent(NotificationEvent event) {
        notificationService.sendNotification(event);
    }
}
