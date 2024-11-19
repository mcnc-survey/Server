package api.mcnc.surveyadminservice.filter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 4:12
 */
@Component
public class ServiceAuthenticationFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String apiKey = exchange.getRequest().getHeaders().getFirst("X-GATEWAY-KEY");

    if (apiKey == null || !apiKey.equals("expected-key")) {
      exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
      return exchange.getResponse().setComplete();
    }

    return chain.filter(exchange);
  }
}
