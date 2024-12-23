package api.mcnc.surveynotificationservice.service;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseEmitterService {

    private final SseEmitterRepository sseEmitterRepository;

    @Value("${sse.timeout}")
    private Long timeout;

    public SseEmitter createEmitter(String emitterKey) {
        return sseEmitterRepository.save(emitterKey, new SseEmitter(timeout));
    }

    public void deleteEmitter(String emitterKey) {
        sseEmitterRepository.deleteById(emitterKey);
    }

    public void sendNotificationToClient(String emitterKey, NotificationDto notificationDto) {
        sseEmitterRepository.findById(emitterKey)
                .ifPresent(emitter -> send(notificationDto, emitterKey, emitter));
    }

    public void send(Object data, String emitterKey, SseEmitter sseEmitter) {
        try {
            log.info("send to client {}:[{}]", emitterKey, data);
            sseEmitter.send(SseEmitter.event()
                    .id(emitterKey)
                    .data(data, MediaType.APPLICATION_JSON));
        } catch (IOException | IllegalStateException e) {
            log.error("IOException | IllegalStateException is occurred. ", e);
            sseEmitterRepository.deleteById(emitterKey);
        }
    }

}
