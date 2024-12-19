package api.mcnc.surveyrespondentservice.client.email;

import api.mcnc.surveyrespondentservice.controller.response.EmailVerifyCheckResponse;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오전 10:55
 */
public interface EmailValidateUseCase {
  void requestVerificationCode(String email);
  EmailVerifyCheckResponse verifyCode(String email, String code);
  boolean isValidEmail(String email);
}
