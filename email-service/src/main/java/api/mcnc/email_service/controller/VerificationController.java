package api.mcnc.email_service.controller;


import api.mcnc.email_service.service.EmailService;
import api.mcnc.email_service.service.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class VerificationController {

    @Autowired
    private EmailService emailService;

    // 인증 코드 요청 API
    @GetMapping("/request-verification-code")
    public String requestVerificationCode(@RequestParam String email) {
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendSingleVerificationEmail(email, verificationCode);
        emailService.saveVerificationCode(email, verificationCode);

        return "인증 코드 발송 완료.";
    }



    @PostMapping("/multi-test")
    public String requestVerificationCode(@RequestBody List<String> emails) {
        // 각 이메일에 대해 고유한 인증 코드를 생성
        for (int i = 0; i < emails.size(); i++) {
            String email = emails.get(i);
            String verificationCode = emailService.generateMultiVerificationCode(i + 1);  // +1, +2, +3을 더하여 인증 코드 생성
            emailService.sendSingleVerificationEmail(email, verificationCode);  // 인증 코드 발송
            emailService.saveVerificationCode(email, verificationCode);  // 인증 코드 저장
        }
        return "인증 코드 발송 완료.";
    }


    // 인증 코드 검증 API
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String email, @RequestParam String code) {
        VerificationCode storedCode = emailService.getVerificationCode(email);//해당 이메일에서 해당 인증UUID가져오기

        if (storedCode == null) {
            return "이메일에 대한 인증 요청을 찾을 수 없습니다.";
        }

        if (!storedCode.getCode().equals(code)) {
            return "유효하지 않은 인증 코드입니다.";
        }

        if (storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            return "인증 코드가 만료되었습니다.";
        }

        return "인증 성공!";
    }
}

