package api.mcnc.surveyrespondentservice.common.audit.authentication;

import java.util.Optional;

public interface RequestedByProvider {
    Optional<String> requestedBy();
}