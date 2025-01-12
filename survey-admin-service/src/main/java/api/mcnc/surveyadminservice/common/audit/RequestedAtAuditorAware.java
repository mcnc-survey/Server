package api.mcnc.surveyadminservice.common.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * JPA Auditing을 위한 클래스<br>
 * 현재 시간 저장 
 * @author 유희준
 */
@Component
public record RequestedAtAuditorAware() implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(ZonedDateTime.now());
    }
}
