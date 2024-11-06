package api.mcnc.survey.common.audit.authentication;

public record RequestedBy(String requestedBy) implements Authentication {
}
