package api.mcnc.survey.common.audit;

import api.mcnc.survey.common.audit.authentication.RequestedByProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public record RequestedByAuditorAware(ApplicationContext ctx) implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(ctx.getBean(RequestedByProvider.class)).flatMap(RequestedByProvider::getRequestBy);
        } catch (Exception e) {
            return Optional.of("system");
        }
    }
}
