package api.mcnc.email_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 동시 처리 가능한 쓰레드 수
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50); // 대기 중인 작업 큐 용량
        executor.setThreadNamePrefix("EmailTask-");
        executor.initialize();
        return executor;
    }
}
