package api.mcnc.email_service.service;

import java.time.LocalDateTime;

public class VerificationCode {
    private String code;
    private LocalDateTime expirationTime;

    public VerificationCode(String code, LocalDateTime expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
}
