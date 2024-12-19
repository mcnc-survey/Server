package api.mcnc.surveyadminservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-17 오후 9:34
 */
@Service
@RequiredArgsConstructor
public class EmailClientService {

  private static final String URI = "https://mcnc-survey-client.vercel.app/help/reset";
  private static final String KEY_URL = "dynamicLink";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_USER_NAME = "userName";
  private static final String KEY_TOKEN = "token";
  private final EmailClient emailClient;

  public String requestVerificationCode(String email) {
    return emailClient.requestVerificationCode(email);
  }

  public boolean verifyCode(String email, String code) {
    return emailClient.verifyCode(email, code);
  }

  public boolean isValidEmail(String email) {
    return emailClient.isValidDeleteCodes(email);
  }

  public String sendPWEmails(String email, String name, String token) {
    Map<String, Object> sendData = Map.of(
      KEY_URL, URI,
      KEY_EMAIL, email,
      KEY_USER_NAME, name,
      KEY_TOKEN, token
    );
    return emailClient.sendPWEmails(sendData).getBody();
  }

}
