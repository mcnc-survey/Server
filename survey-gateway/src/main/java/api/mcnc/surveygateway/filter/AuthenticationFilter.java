package api.mcnc.surveygateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @LoadBalanced
    private final WebClient webClient;

    public final static String URI ="/token-validation";

    public AuthenticationFilter(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        super(Config.class);
        this.webClient = WebClient.builder()
                .filter(lbFunction)
                .baseUrl("http://survey-admin-service")
                .build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return validateToken(token)
                        .flatMap(response -> proceedWithUserId(response.adminId(), exchange, chain))
                        .switchIfEmpty(chain.filter(exchange)) // If token is invalid, continue without setting userId
                        .onErrorResume(e -> handleAuthenticationError(exchange, e)); // Handle errors
            }

            if (authHeader == null) {
                return handleAuthenticationError(exchange, new RuntimeException("Authorization header is missing"));
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleAuthenticationError(ServerWebExchange exchange, Throwable e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<TokenValidateResponse> validateToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        return webClient.post()
          .uri(URI)
          .bodyValue("{\"accessToken\":\"" + token + "\"}")
          .header("Content-Type", "application/json")
          .retrieve()
          .bodyToMono(TokenValidateResponse.class);
    }

    private Mono<Void> proceedWithUserId(String adminId, ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest().mutate().header("requested-by", adminId);
        return chain.filter(exchange);
    }

    public static class Config {
        // 필터 구성을 위한 설정 클래스
    }
}
