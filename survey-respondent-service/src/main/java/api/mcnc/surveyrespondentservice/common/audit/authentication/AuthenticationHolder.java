package api.mcnc.surveyrespondentservice.common.audit.authentication;

import java.util.Optional;

public interface AuthenticationHolder {
    Optional<Authentication> getAuthentication();
    void setAuthentication(Authentication authentication);
}
