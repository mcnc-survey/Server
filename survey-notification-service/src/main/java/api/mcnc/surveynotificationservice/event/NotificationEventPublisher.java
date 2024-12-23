package api.mcnc.surveynotificationservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(NotificationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
