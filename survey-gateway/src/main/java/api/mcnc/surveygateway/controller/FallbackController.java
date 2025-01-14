package api.mcnc.surveygateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * fall back
 *
 * @author :유희준
 * @since :2024-11-18 오후 5:40
 */
@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/authFailure")
    public Mono<Map<String, Object>> authFailure() {
        return Mono.just(Map.of("status", "down"));
    }
}