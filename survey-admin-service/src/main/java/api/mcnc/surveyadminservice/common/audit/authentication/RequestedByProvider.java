package api.mcnc.surveyadminservice.common.audit.authentication;

import java.util.Optional;

public interface RequestedByProvider {
    Optional<String> requestedBy();
}
