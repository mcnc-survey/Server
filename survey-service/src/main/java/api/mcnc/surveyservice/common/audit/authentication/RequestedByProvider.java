package api.mcnc.surveyservice.common.audit.authentication;

import java.util.Optional;

public interface RequestedByProvider {
    Optional<String> requestedBy();
}
