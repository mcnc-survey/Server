package api.mcnc.surveyadminservice.common.audit.authentication;

/**
 * 요청자 정보
 * @author 유희준
 */
public record RequestedBy(String requestedBy) implements Authentication {
}
