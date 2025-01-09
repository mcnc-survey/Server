package api.mcnc.surveyservice.config;

import api.mcnc.surveyservice.domain.Survey;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2025-01-09 오후 1:28
 */
@Configuration
@EnableCaching // Spring Boot의 캐싱 설정을 활성화
public class RedisCacheConfig {

  @Bean
  public CacheManager surveyCacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
    ObjectMapper om = JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .activateDefaultTyping(
        objectMapper.getPolymorphicTypeValidator(),
        ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS,
        JsonTypeInfo.As.WRAPPER_OBJECT
      )
      .build();


    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
      .defaultCacheConfig()
      // Redis에 Key를 저장할 때 String으로 직렬화(변환)해서 저장
      .serializeKeysWith(
        RedisSerializationContext.SerializationPair.fromSerializer(
          new StringRedisSerializer()))
      // Redis에 Value를 저장할 때 Json으로 직렬화(변환)해서 저장
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(
          new GenericJackson2JsonRedisSerializer(om)
        )
      )
      // 데이터의 만료기간(TTL) 설정
      .entryTtl(Duration.ofMinutes(30L));

    return RedisCacheManager
      .RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(redisCacheConfiguration)
      .build();
  }
}
