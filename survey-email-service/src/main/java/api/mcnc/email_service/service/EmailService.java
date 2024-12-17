package api.mcnc.email_service.service;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private String verificationEmailTemplate;
    private String inviteEmailTemplate;
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();


    // 애플리케이션 시작 시 템플릿을 메모리에 로드
    @PostConstruct
    public void loadEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/VerificationEmail.html");
        verificationEmailTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // 프로젝트 초대 이메일 템플릿 로드
        ClassPathResource inviteResource = new ClassPathResource("templates/Email.html");
        inviteEmailTemplate = StreamUtils.copyToString(inviteResource.getInputStream(), StandardCharsets.UTF_8);

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


//    public void sendSingleVerificationEmail(String toEmail, String verificationCode) throws IOException, MessagingException {
//
//
//        ClassPathResource resource = new ClassPathResource("templates/VerificationEmail.html");
//        String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
//
//        // 인증 코드 삽입
//        String htmlContent = htmlTemplate.replace("{{verificationCode}}", verificationCode);
//
//        // MimeMessage 생성 및 설정
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        // 이메일 수신자 설정
//        helper.setTo(toEmail);
//
//        // 이메일 제목 및 본문 설정
//        helper.setSubject("인증 코드 발송");
//        helper.setText(htmlContent, true); // HTML 형식 사용
//
//        // 이메일 전송
//        mailSender.send(message);
//    }

    // 이메일을 비동기적으로 보내는 메서드
    @Async
    public void sendSingleVerificationEmail(String toEmail, String verificationCode) {
        try {
            // 인증 코드 삽입
            String htmlContent = verificationEmailTemplate.replace("{{verificationCode}}", verificationCode);

            // MimeMessage 설정
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail); // 수신자 설정
            helper.setSubject("인증 코드 발송"); // 제목 설정
            helper.setText(htmlContent, true); // HTML 형식 본문 설정

            mailSender.send(message); // 이메일 전송
            log.info("Email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }

    // 여러 이메일에 대해 비동기 전송
    public void sendVerificationEmails(List<String> toEmails, String verificationCode) {
        toEmails.parallelStream().forEach(toEmail -> sendSingleVerificationEmail(toEmail, verificationCode));
    }
    
    //찾았다 저장 뛰발럼
    public void saveVerificationCode(String email, String code) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        verificationCodes.put(email, new VerificationCode(code, expirationTime));
    }

    
    //뭐더라
    public VerificationCode getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    
//    //뭐더라
//    public void sendInviteEmails(List<String> toEmails, String userName, String projectName, String dynamicLink) throws MessagingException, IOException {
//        // ClassPathResource를 사용해 resources 디렉토리의 파일 읽기
//        ClassPathResource resource = new ClassPathResource("templates/Email.html");
//        String htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
//
//        // 동적 데이터 삽입
//        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm"));
//        String htmlContent = htmlTemplate
//                .replace("{{userName}}", userName)
//                .replace("{{projectName}}", projectName)
//                .replace("{{date}}", currentDate)
//                .replace("{{dynamicLink}}", dynamicLink);
//
//        for (String toEmail : toEmails) {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setTo(toEmail);
//            helper.setSubject("프로젝트 초대 알림");
//            helper.setText(htmlContent, true);
//
//            mailSender.send(message);
//        }
//    }

    // 프로젝트 초대 이메일을 비동기적으로 보내는 메서드
    public void sendInviteEmail(String toEmail, String userName, String projectName, String dynamicLink) {
        try {
            // 동적 데이터 삽입
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm"));
            String htmlContent = inviteEmailTemplate
                    .replace("{{userName}}", userName)
                    .replace("{{projectName}}", projectName)
                    .replace("{{date}}", currentDate)
                    .replace("{{dynamicLink}}", dynamicLink);

            // MimeMessage 생성 및 설정
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail); // 수신자
            helper.setSubject("프로젝트 초대 알림"); // 제목
            helper.setText(htmlContent, true); // HTML 형식 본문 설정

            mailSender.send(message); // 이메일 전송
            log.info("Invite email sent to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send invite email to {}: {}", toEmail, e.getMessage());
        }
    }

    // 여러 초대 이메일을 비동기적으로 전송하는 메서드
    @Async
    public void sendInviteEmails(List<String> toEmails, String userName, String projectName, String dynamicLink) {
        toEmails.parallelStream().forEach(toEmail -> {
            sendInviteEmail(toEmail, userName, projectName, dynamicLink);
        });
    }


}