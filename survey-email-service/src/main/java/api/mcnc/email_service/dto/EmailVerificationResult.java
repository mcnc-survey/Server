package api.mcnc.email_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 이메일 인증 결과를 나타내는 DTO 클래스
 * @author 차익현
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationResult {
    private String email;
    private boolean success;
    private String message;
}
