package api.mcnc.surveyresponseservice.common.audit.authentication;

public record RequestedBy(String requestedBy) implements Authentication {
}
