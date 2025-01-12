package api.mcnc.surveyadminservice.common.audit.authentication;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 요청 정보, 인증 정보 구현체
 * @author 유희준
 */
@Component
public class AuthenticationHolderImpl implements AuthenticationHolder, RequestedByProvider {
    private Authentication authentication;

    @Override
    public Optional<Authentication> getAuthentication() {
        return Optional.of(this.authentication);
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Optional<String> requestedBy() {
        return getAuthentication()
                .map(Authentication::requestedBy);
    }
}
