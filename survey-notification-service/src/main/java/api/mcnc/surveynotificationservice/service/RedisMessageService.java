package api.mcnc.surveynotificationservice.service;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.service.handler.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;


/**
 * Redis 메시지 처리 및 구독/발행 서비스를 제공하는 클래스
 * @author 차익현
 */
@RequiredArgsConstructor
@Service
public class RedisMessageService {

    private final RedisMessageListenerContainer container;
    private final RedisSubscriber subscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 주어진 채널에 대해 구독을 시작
     *
     * @param channel 채널명
     */
    public void subscribe(String channel) {
        container.addMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    /**
     * 주어진 채널에 알림 메시지를 발행
     *
     * @param channel 채널명
     * @param notificationDto 알림 메시지 DTO
     */
    public void publish(String channel, NotificationDto notificationDto) {
        redisTemplate.convertAndSend(getChannelName(channel), notificationDto);
    }

    /**
     * 주어진 채널에 대한 구독을 종료
     *
     * @param channel 채널명
     */
    public void removeSubscribe(String channel) {
        container.removeMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    /**
     * 채널 이름에 접두어 "channel:"을 붙여 반환
     *
     * @param id 채널 ID
     * @return channel: 채널 이름
     */
    private String getChannelName(String id) {
        return "channel:" + id;
    }
}
