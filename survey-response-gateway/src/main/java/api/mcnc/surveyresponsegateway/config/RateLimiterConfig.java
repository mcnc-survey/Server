package api.mcnc.surveyresponsegateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * ratelimit
 *
 * @author :유희준
 * @since :2024-11-10 오후 6:40
 */
@Configuration
public class RateLimiterConfig {

  @Bean(name = "ipKeyResolver")
  public KeyResolver ipKeyResolver() {
//    요청을 식별하는 데 사용되는 키를 결정하는 역할
    return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
  }

}
