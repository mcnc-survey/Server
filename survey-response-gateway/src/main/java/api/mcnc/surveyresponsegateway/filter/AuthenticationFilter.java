package api.mcnc.surveyresponsegateway.filter;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 인증/인가
 * @author :유희준
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  @LoadBalanced
  private final WebClient webClient;

  public AuthenticationFilter(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
    super(Config.class);
    this.webClient = WebClient.builder()
      .filter(lbFunction)
      .baseUrl("http://survey-respondent-service")
      .build();
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

      if (authHeader == null) {
        return handleAuthenticationError(exchange);
      }

      if (authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        return validateToken(token)
          // TODO 2024-12-18 yhj : 필터에서 userId, respondentId
          .flatMap(tokenExtract -> {
            if (exchange.getRequest().getPath().toString().equals("/responses")) {
              return proceedWithUserIdAndRewritePath(tokenExtract, exchange, chain);
            }
            return proceedWithUserId(tokenExtract.respondentId(), exchange, chain);
          })
          .switchIfEmpty(chain.filter(exchange)) // If token is invalid, continue without setting userId
          .onErrorResume(e -> handleAuthenticationError(exchange)); // Handle errors
      }

      // Authorization 헤더가 유효하지 않은 경우
      return handleAuthenticationError(exchange);
    };
  }

  private Mono<Void> handleAuthenticationError(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  private Mono<TokenExtractResponse> validateToken(String token) {
    return webClient.post()
      .uri("/token-validation")
      .bodyValue("{\"accessToken\":\"" + token + "\"}")
      .header("Content-Type", "application/json")
      .retrieve()
      .bodyToMono(TokenExtractResponse.class);
  }

  private Mono<Void> proceedWithUserIdAndRewritePath(TokenExtractResponse tokenExtract, ServerWebExchange exchange, GatewayFilterChain chain) {
    String respondentId = tokenExtract.respondentId();
    String surveyId = tokenExtract.surveyId();

    // 새 경로 생성: /responses/<respondentId>
    String newPath = "/responses/" + surveyId;

    // 요청의 URI를 변경
    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
      .path(newPath)
      .build();

    // 새로운 요청으로 교체
    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
    mutatedExchange.getRequest().mutate().header("requested-by", respondentId);
    return chain.filter(mutatedExchange);
  }

  private Mono<Void> proceedWithUserId(String respondentId, ServerWebExchange exchange, GatewayFilterChain chain) {
    exchange.getRequest().mutate().header("requested-by", respondentId);
    return chain.filter(exchange);
  }

  public static class Config {
    // 필터 구성을 위한 설정 클래스
  }
}
