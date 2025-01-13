package api.mcnc.surveygateway.config;

import api.mcnc.surveygateway.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * exception handler
 *
 * @author :유희준
 * @since :2024-11-18 오후 2:40
 */
@Configuration
@RequiredArgsConstructor
public class ErrorExceptionConfig {
    private final ObjectMapper objectMapper;
    @Bean
    public ErrorWebExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler(objectMapper);
    }
}