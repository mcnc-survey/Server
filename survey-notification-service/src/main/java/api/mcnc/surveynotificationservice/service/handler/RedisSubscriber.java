package api.mcnc.surveynotificationservice.service.handler;

import static api.mcnc.surveynotificationservice.constants.Channel.CHANNEL_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.listener.Message;
//import org.springframework.data.redis.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseEmitterService sseEmitterService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 채널 이름에서 접두어(CHANNEL_PREFIX)를 제외한 채널 이름을 추출
            String channel = new String(message.getChannel())
                    .substring(CHANNEL_PREFIX.length());

            // Redis 메시지의 본문을 NotificationDto 객체로 변환
            NotificationDto notificationDto = objectMapper.readValue(message.getBody(),
                    NotificationDto.class);

            System.out.println(notificationDto);

            // 해당 채널에 연결된 클라이언트에게 알림을 전송
            sseEmitterService.sendNotificationToClient(channel, notificationDto);
        } catch (IOException e) {
            log.error("IOException occurred while processing the message.", e);
        }
    }
}
