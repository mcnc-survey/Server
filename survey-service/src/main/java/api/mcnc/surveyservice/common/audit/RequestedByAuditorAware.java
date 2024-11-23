package api.mcnc.surveyservice.common.audit;

import api.mcnc.surveyresponseservice.common.audit.authentication.RequestedByProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public record RequestedByAuditorAware(ApplicationContext applicationContext) implements AuditorAware<String> {
    private static final String SYSTEM = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            RequestedByProvider provider = applicationContext.getBean(RequestedByProvider.class);
            return provider.requestedBy().or(() -> Optional.of(SYSTEM));

        } catch (Exception e) {
            return Optional.of(SYSTEM); // 입력되지 않은 경우에는 기본값 "system" 으로 사용
        }
    }
}
