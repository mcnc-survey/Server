package api.mcnc.email_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, VerificationCode> verificationCodes = new HashMap<>();

    public void sendMultiVerificationEmail(List<String> emails, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("인증 코드 발송");
        message.setText("인증 코드 6자리: " + verificationCode + "\n\n1분 내로 입력해주십시오.");

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

        // 마지막 문자의 아스키 코드 값 구하기
        int asciiValue = (int) lastChar;

        // 아스키 코드 값에 index를 더한 값으로 새로운 문자 생성
        char newChar = (char) (asciiValue + index);

        // 마지막 문자만 수정한 새로운 인증 코드 생성
        String newVerificationCode = uuid.substring(0, uuid.length() - 1) + newChar;

        return newVerificationCode;  // 최종 인증 코드 반환
    }


    public void sendSingleVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("인증 코드 발송");
        message.setText("인증 코드 6자리: " + verificationCode + "\n\n1분 내로 입력해주십시오.");
        mailSender.send(message);
    }

    public void saveVerificationCode(String email, String code) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);
        verificationCodes.put(email, new VerificationCode(code, expirationTime));
    }

    public VerificationCode getVerificationCode(String email) {
        return verificationCodes.get(email);
    }
}