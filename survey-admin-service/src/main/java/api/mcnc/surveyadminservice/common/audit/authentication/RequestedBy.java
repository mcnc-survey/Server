package api.mcnc.surveyadminservice.common.audit.authentication;

public record RequestedBy(String requestedBy) implements Authentication {
}
