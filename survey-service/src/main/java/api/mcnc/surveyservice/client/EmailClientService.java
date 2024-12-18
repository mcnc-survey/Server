package api.mcnc.surveyservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-18 오후 4:13
 */
@Service
@RequiredArgsConstructor
public class EmailClientService {

  public static final String KEY_URL = "dynamicLink";
  public static final String KEY_EMAIL_LIST = "emails";
  public static final String KEY_PROJECT_NAME = "projectName";

  private final EmailClient emailClient;

  public void sendHtmlVerificationEmails(String title, String inviteUrl, List<String> emails) {
    Map<String, Object> sendData = Map.of(
      KEY_URL, inviteUrl,
      KEY_EMAIL_LIST, emails,
      KEY_PROJECT_NAME, title
    );
    emailClient.sendHtmlVerificationEmails(sendData);
  }
}
