package api.mcnc.email_service.service;

import java.time.LocalDateTime;

public class VerificationCode {
    private String code;
    private LocalDateTime expirationTime;
    private boolean isVerified;

    public VerificationCode(String code, LocalDateTime expirationTime, boolean isVerified) {
        this.code = code;
        this.expirationTime = expirationTime;
        this.isVerified = false;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
