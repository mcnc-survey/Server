package api.mcnc.email_service.controller;


import api.mcnc.email_service.dto.HtmlEmailRequest;
import api.mcnc.email_service.service.EmailService;
import api.mcnc.email_service.service.VerificationCode;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class VerificationController {

    @Autowired
    private EmailService emailService;

    // 인증 코드 요청 API
    @GetMapping("/request-verification-code")
    public String requestVerificationCode(@RequestParam String email) throws MessagingException, IOException {
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendSingleVerificationEmail(email, verificationCode);
        emailService.saveVerificationCode(email, verificationCode);

        return "인증 코드 발송 완료.";
    }




    @PostMapping("/multi-test")
    public ResponseEntity<String> requestVerificationCode(@RequestBody List<String> emails) {
        try {
            // 이메일 목록이 비어 있으면 오류 반환
            if (emails == null || emails.isEmpty()) {
                return ResponseEntity.badRequest().body("이메일 목록이 비어 있습니다.");
            }

            // 각 이메일에 대해 고유한 인증 코드를 생성하고 HTML 이메일 발송
            for (int i = 0; i < emails.size(); i++) {
                String email = emails.get(i);
                String verificationCode = emailService.generateMultiVerificationCode(i + 1); // +1, +2, +3으로 고유 코드 생성

                // HTML 이메일 발송 및 저장
                emailService.sendSingleVerificationEmail(email, verificationCode);
                emailService.saveVerificationCode(email, verificationCode);
            }

            return ResponseEntity.ok("모든 이메일로 인증 코드가 발송되었습니다.");
        } catch (Exception e) { // 일반적인 Exception 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
        }
    }






    // 인증 코드 검증 API
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email, @RequestParam String code) {
        VerificationCode storedCode = emailService.getVerificationCode(email);//해당 이메일에서 해당 인증UUID가져오기

        if (storedCode == null) {
            return "해당 이메일에 대한 인증 요청을 찾을 수 없습니다.";
        }

        if (!storedCode.getCode().equals(code)) {
            return "유효하지 않은 인증 코드입니다.";
        }

        if (storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            return "인증 코드가 만료되었습니다.";
        }

        return "인증 성공!";
    }


    @PostMapping("/html-email")
    public ResponseEntity<String> sendHtmlVerificationEmails(@RequestBody Map<String, Object> request) {
        try {
            // Map에서 데이터 추출
            List<String> emails = (List<String>) request.get("emails");
            String userName = (String) request.get("userName");
            String projectName = (String) request.get("projectName");
            String dynamicLink = (String) request.get("dynamicLink");

            // 이메일 서비스 메서드 호출
            emailService.sendHtmlVerificationEmails(emails, userName, projectName, dynamicLink);

            return ResponseEntity.ok("HTML 이메일 전송 완료.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("템플릿 파일 읽기 실패: " + e.getMessage());
        } catch (ClassCastException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 데이터 형식이 올바르지 않습니다.");
        }
    }



}

