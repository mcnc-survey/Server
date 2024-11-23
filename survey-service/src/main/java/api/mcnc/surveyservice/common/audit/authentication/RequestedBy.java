package api.mcnc.surveyservice.common.audit.authentication;

public record RequestedBy(String requestedBy) implements Authentication {
}
