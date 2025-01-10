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

/**
 * SSE Emitter를 관리하고 클라이언트로 알림을 전송하는 서비스 클래스
 * @author 차익현
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SseEmitterService {

    private final SseEmitterRepository sseEmitterRepository;

    @Value("${sse.timeout}")
    private Long timeout;

    /**
     * 새로운 SseEmitter를 생성하고 저장소에 저장
     *
     * @param emitterKey 고유 식별 키
     * @return SseEmitter 객체
     */
    public SseEmitter createEmitter(String emitterKey) {
        return sseEmitterRepository.save(emitterKey, new SseEmitter(timeout));
    }

    /**
     * 특정 SseEmitter를 삭제
     *
     * @param emitterKey 고유 식별 키
     */
    public void deleteEmitter(String emitterKey) {
        sseEmitterRepository.deleteById(emitterKey);
    }

    /**
     * 특정 클라이언트로 알림을 전송
     *
     * @param emitterKey 고유 식별 키
     * @param notificationDto 알림 메시지 DTO
     */
    public void sendNotificationToClient(String emitterKey, NotificationDto notificationDto) {
        sseEmitterRepository.findById(emitterKey)
                .ifPresent(emitter -> send(notificationDto, emitterKey, emitter));
    }

    /**
     * 클라이언트로 데이터를 전송, 전송 실패 시 해당 SseEmitter를 삭제
     *
     * @param data 전송할 데이터
     * @param emitterKey 고유 식별 키
     * @param sseEmitter 데이터를 전송할 고유 식별 키
     */
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
