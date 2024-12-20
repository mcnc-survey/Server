package api.mcnc.surveynotificationservice.service;

import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.service.handler.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisMessageService {

    private final RedisMessageListenerContainer container;
    private final RedisSubscriber subscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    public void subscribe(String channel) {
        container.addMessageListener((MessageListener) subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    public void publish(String channel, NotificationDto notificationDto) {
        redisTemplate.convertAndSend(getChannelName(channel), notificationDto);
    }

    public void removeSubscribe(String channel) {
        container.removeMessageListener((MessageListener) subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    private String getChannelName(String id) {
        return "channel:" + id;
    }
}
