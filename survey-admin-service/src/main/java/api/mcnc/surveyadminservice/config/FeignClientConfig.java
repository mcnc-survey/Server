package api.mcnc.surveyadminservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오후 8:23
 */
@Configuration
@EnableFeignClients(basePackages = "api.mcnc.surveyadminservice.client")
public class FeignClientConfig {
}
