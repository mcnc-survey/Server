package api.mcnc.surveyadminservice.common.audit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(
        auditorAwareRef = "requestedByAuditorAware",
        dateTimeProviderRef = "requestedAtAuditorAware"
)
public class JpaAuditConfig {
}
