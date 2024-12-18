package api.mcnc.email_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleVerificationResponse {
    private boolean success;
    private String message;
    private List<EmailVerificationResult> results;
}
