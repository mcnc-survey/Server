package api.mcnc.surveyrespondentservice.common.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

// TODO 2024-12-03 yhj : 공통 모듈로 분리 해야함 시간 나면..?
@Component
public record RequestedAtAuditorAware() implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now());
    }
}
