package api.mcnc.survey.common.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component
public record RequestedAtAuditorAware() implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now());
    }
}