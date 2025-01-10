package api.mcnc.email_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 다중 이메일 인증 결과 응답 DTO 클래스
 * @author 차익현
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleVerificationResponse {
    private boolean success;
    private String message;
    private List<EmailVerificationResult> results;
}
