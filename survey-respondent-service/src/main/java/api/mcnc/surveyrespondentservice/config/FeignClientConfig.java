package api.mcnc.surveyrespondentservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * feign 설정
 *
 * @author :유희준
 * @since :2024-11-21 오후 8:23
 */
@Configuration
@EnableFeignClients(basePackages = "api.mcnc.surveyrespondentservice.client")
public class FeignClientConfig {
}
