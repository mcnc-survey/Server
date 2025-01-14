package api.mcnc.surveyadminservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 이메일 서비스에 요청하는 서비스
 *
 * @author :유희준
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

  /**
   * 이메일 인증 코드 요청
   * @param email 이메일
   * @return 인증 코드
   */
  public String requestVerificationCode(String email) {
    return emailClient.requestVerificationCode(email);
  }

  /**
   * 이메일 인증 코드 검증
   * @param email 이메일
   * @param code 인증 코드
   * @return 검증 결과
   */
  public boolean verifyCode(String email, String code) {
    return emailClient.verifyCode(email, code);
  }

  /**
   * 이메일 인증 검증이 됐는지 검증
   * @param email 이메일
   * @return 검증 결과
   */
  public boolean isValidEmail(String email) {
    return emailClient.isValidDeleteCodes(email);
  }

  /**
   * 비밀번호 초기화 메일 보내기
   * @param email 이메일
   * @param name 이름
   * @param token 토큰
   * @return 메일 보내기 결과
   */
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
