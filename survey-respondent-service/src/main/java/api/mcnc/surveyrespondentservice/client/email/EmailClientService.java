package api.mcnc.surveyrespondentservice.client.email;

import api.mcnc.surveyrespondentservice.controller.response.EmailVerifyCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * email service
 *
 * @author :유희준
 * @since :2024-12-19 오전 10:49
 */
@Service
@RequiredArgsConstructor
public class EmailClientService implements EmailValidateUseCase{

  private final EmailClient emailClient;

  @Override
  public void requestVerificationCode(String email) {
    emailClient.requestVerificationCode(email);
  }

  @Override
  public EmailVerifyCheckResponse verifyCode(String email, String code) {
    boolean verifyResult = emailClient.verifyCode(email, code);
    return new EmailVerifyCheckResponse(verifyResult);
  }

  @Override
  public boolean isValidEmail(String email) {
    return emailClient.isValidDeleteCodes(email);
  }

}
