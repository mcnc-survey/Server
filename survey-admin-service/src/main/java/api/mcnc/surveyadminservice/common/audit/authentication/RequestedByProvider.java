package api.mcnc.surveyadminservice.common.audit.authentication;

import java.util.Optional;

/**
 * 요청자 정보 제공 인터페이스
 * @author 유희준
 */
public interface RequestedByProvider {
    Optional<String> requestedBy();
}
