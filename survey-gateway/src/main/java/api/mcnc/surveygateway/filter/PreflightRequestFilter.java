package api.mcnc.surveygateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * prefligth 설정
 *
 * @author :유희준
 * @since :2024-12-30 오후 5:46
 */
@Component
public class PreflightRequestFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
      exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
      exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
      exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
      exchange.getResponse().getHeaders().add("Access-Control-Max-Age", "3600"); // 캐싱 지속 시간 설정
      return exchange.getResponse().setComplete();
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return -1; // 다른 필터보다 먼저 실행
  }
}
