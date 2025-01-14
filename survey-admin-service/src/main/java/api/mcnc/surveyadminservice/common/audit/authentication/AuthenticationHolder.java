package api.mcnc.surveyadminservice.common.audit.authentication;

import java.util.Optional;

/**
 * 인증 정보를 저장하는 인터페이스
 * @author 유희준
 */
public interface AuthenticationHolder {
    Optional<Authentication> getAuthentication();
    void setAuthentication(Authentication authentication);
}
