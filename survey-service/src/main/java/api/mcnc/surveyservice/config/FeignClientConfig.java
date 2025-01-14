package api.mcnc.surveyservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-21 오후 8:23
 */
@Configuration
@EnableFeignClients(basePackages = "api.mcnc.surveyservice.client")
public class FeignClientConfig {
}
