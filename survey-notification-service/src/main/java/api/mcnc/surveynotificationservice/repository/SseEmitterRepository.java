package api.mcnc.surveynotificationservice.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE 연결을 관리하는 리포지토리
 * @author 차익현
 * */
@Repository
public class SseEmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 새로운 SseEmitter를 저장
    public SseEmitter save(String eventId, SseEmitter sseEmitter) {
        emitters.put(eventId, sseEmitter);
        return sseEmitter;
    }

    // 주어진 ID로 SseEmitter 조회
    public Optional<SseEmitter> findById(String memberId) {
        return Optional.ofNullable(emitters.get(memberId));
    }

    // 주어진 ID로 SseEmitter 삭제
    public void deleteById(String eventId) {
        emitters.remove(eventId);
    }
}
