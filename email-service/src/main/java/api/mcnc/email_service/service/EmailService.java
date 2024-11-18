package api.mcnc.email_service.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final Map<String, VerificationCode> verificationCodes = new HashMap<>();

    public void sendMultiVerificationEmail(List<String> emails, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("인증 코드 발송");
        message.setText("인증 코드 6자리: " + verificationCode + "\n\n5분 내로 입력해주십시오.");

        // 다수의 수신자 설정
        String[] emailArray = emails.toArray(new String[0]);
        message.setTo(emailArray);

        mailSender.send(message);
    }



    public String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);  // 앞의 6자리만 사용
    }


    public String generateMultiVerificationCode(int index) {
        // UUID를 생성하고, 앞의 6자리만 사용
        String uuid = UUID.randomUUID().toString().substring(0, 6);

        // 마지막 문자 가져오기
        char lastChar = uuid.charAt(uuid.length() - 1);

        // 아스키 코드 값에 index를 더한 값으로 새로운 문자 생성
        char newChar = (char) ((int) lastChar + index);

        return uuid.substring(0, uuid.length() - 1) + newChar;  // 최종 인증 코드 반환
    }


    public void sendSingleVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("인증 코드 발송");
        message.setText("인증 코드 6자리: " + verificationCode + "\n\n5분 내로 입력해주십시오.");
        mailSender.send(message);
    }

    public void saveVerificationCode(String email, String code) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        verificationCodes.put(email, new VerificationCode(code, expirationTime));
    }

    public VerificationCode getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    public void sendHtmlVerificationEmails(List<String> toEmails, String userName, String projectName, String dynamicLink) throws MessagingException, MessagingException {
        String htmlContentTemplate = "<!DOCTYPE html><html lang='ko'><head><meta charset='UTF-8'><style>"
                + ".container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif; color: #333333; text-align: center; }"
                + ".title { font-size: 18px; font-weight: bold; margin-bottom: 10px; }"
                + ".date { font-size: 12px; color: #888888; margin-bottom: 20px; }"
                + ".button { display: inline-block; padding: 12px 24px; background-color: #4CAF50; color: #ffffff; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; margin-top: 20px; }"
                + ".footer { font-size: 12px; color: #999999; margin-top: 20px; }"
                + "</style></head><body>"
                + "<div class='container'>"
                + "<p class='title'><strong>" + userName + "</strong>님이 <strong>" + projectName + "</strong> 프로젝트에 초대했습니다.</p>"
                + "<p class='date'>" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm")) + "</p>"
                + "<table role='presentation' style='width: 100%; margin-top: 20px;'>"
                + "<tr><td align='center'><a href='" + dynamicLink + "' class='button'>페이지로 이동</a></td></tr>"
                + "</table>"
                + "<div class='footer'><p>사이트 이름</p><p>설문 사이트 뭐시기</p>"
                + "<a href='https://notion.so' style='color: #9e9e9e; text-decoration: underline;'>사이트 링크</a></div>"
                + "</div></body></html>";

        for (String toEmail : toEmails) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("프로젝트 초대 알림");
            helper.setText(htmlContentTemplate, true);

            mailSender.send(message);
        }
    }
}