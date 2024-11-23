package api.mcnc.surveyrespondentservice.common.audit.authentication;

public record RequestedBy(String requestedBy) implements Authentication {
}
