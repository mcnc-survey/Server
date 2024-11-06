package api.mcnc.survey.common.audit.authentication;

import java.util.Optional;

public interface RequestedByProvider {
    Optional<String> getRequestBy();
}
