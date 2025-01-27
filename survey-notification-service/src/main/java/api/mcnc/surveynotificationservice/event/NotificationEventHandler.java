package api.mcnc.surveynotificationservice.event;

import api.mcnc.surveynotificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
