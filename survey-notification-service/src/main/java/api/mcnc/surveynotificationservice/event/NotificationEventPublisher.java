package api.mcnc.surveynotificationservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 알림 이벤트를 발행하는 퍼블리셔
 * @author 차익현
 */
@RequiredArgsConstructor
@Component
public class NotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(NotificationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
