package api.mcnc.email_service.controller;


import api.mcnc.email_service.dto.EmailVerificationResult;
import api.mcnc.email_service.dto.MultipleVerificationResponse;
import api.mcnc.email_service.service.EmailService;
import api.mcnc.email_service.service.VerificationCode;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class VerificationController {

    @Autowired
    private EmailService emailService;

    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();


    // 인증 코드 요청 API 이거 안쓰는걸 추천
    @GetMapping("/request-code")
    public String requestVerificationCode(@RequestParam String email) throws MessagingException, IOException {
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendSingleVerificationEmail(email, verificationCode);
        emailService.saveVerificationCode(email, verificationCode);

        return "인증 코드 발송 완료.";
    }



    // 인증 코드 요청 API
//    @PostMapping("/multi-request")
//    public ResponseEntity<String> requestVerificationCode(@RequestBody List<String> emails) {
//        try {
//            // 이메일 목록이 비어 있으면 오류 반환
//            if (emails == null || emails.isEmpty()) {
//                return ResponseEntity.badRequest().body("이메일 목록이 비어 있습니다.");
//            }
//
//            // 각 이메일에 대해 고유한 인증 코드를 생성하고 HTML 이메일 발송
//            for (int i = 0; i < emails.size(); i++) {
//                String email = emails.get(i);
//                String verificationCode = emailService.generateMultiVerificationCode(i + 1); // +1, +2, +3으로 고유 코드 생성
//
//                // HTML 이메일 발송 및 저장
//                emailService.sendSingleVerificationEmail(email, verificationCode);
//                emailService.saveVerificationCode(email, verificationCode);
//            }
//
//            return ResponseEntity.ok("모든 이메일로 인증 코드가 발송되었습니다.");
//        } catch (Exception e) { // 일반적인 Exception 처리
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
//        }
//    }

    @PostMapping("/multi-request")
    public ResponseEntity<MultipleVerificationResponse> requestVerificationCode(@RequestBody List<String> emails) {
        // 이메일 목록이 비어 있으면 오류 반환
        if (CollectionUtils.isEmpty(emails)) {
            return ResponseEntity.badRequest().body(
                    new MultipleVerificationResponse(false, "이메일 목록이 비어 있습니다.", null)
            );
        }

        try {
            // 병렬 스트림을 사용해 이메일 처리 성능 개선
            List<EmailVerificationResult> results = emails.parallelStream()
                    .map(email -> {
                        try {
                            // 인덱스 기반 고유 인증 코드 생성
                            int index = emails.indexOf(email) + 1;
                            String verificationCode = emailService.generateMultiVerificationCode(index);

                            // 이메일 발송 및 코드 저장
                            emailService.sendSingleVerificationEmail(email, verificationCode);
                            emailService.saveVerificationCode(email, verificationCode);

                            return new EmailVerificationResult(email, true, "인증 코드 발송 성공");
                        } catch (Exception e) {
                            log.error("이메일 처리 중 오류: {}", email, e);
                            return new EmailVerificationResult(email, false, e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());

            // 전체 성공 여부 판단
            boolean overallSuccess = results.stream().allMatch(EmailVerificationResult::isSuccess);

            return ResponseEntity.ok(
                    new MultipleVerificationResponse(overallSuccess, "모든 이메일로 인증 코드 발송", results)
            );
        } catch (Exception e) {
            log.error("다중 이메일 검증 중 예상치 못한 오류", e);
            return ResponseEntity.internalServerError().body(
                    new MultipleVerificationResponse(false, "예상치 못한 오류 발생", null)
            );
        }
    }




//    // 인증 코드 검증 API
//    @PostMapping("/verify-code")
//    public String verifyCode(@RequestParam String email, @RequestParam String code) {
//        VerificationCode storedCode = emailService.getVerificationCode(email);//해당 이메일에서 해당 인증UUID가져오기
//
//        if (storedCode == null) {
//            return "해당 이메일에 대한 인증 요청을 찾을 수 없습니다.";
//        }
//
//        if (!storedCode.getCode().equals(code)) {
//            return "유효하지 않은 인증 코드입니다.";
//        }
//
//        if (storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
//            return "인증 코드가 만료되었습니다.";
//        }
//
//        return "인증 성공!";
//    }

    // 인증 코드 검증 API
    @PostMapping("/check-code")
    public boolean verifyCode(@RequestParam String email, @RequestParam String code) {
        VerificationCode storedCode = emailService.getVerificationCode(email); // 해당 이메일에서 인증 UUID 가져오기

        if (storedCode == null) {
            return false; // 인증 요청을 찾을 수 없음
        }

        if (!storedCode.getCode().equals(code)) {
            return false; // 유효하지 않은 인증 코드
        }

        if (storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // 인증 코드 만료
        }

        storedCode.setVerified(true);
        return true; // 인증 성공
    }

    @PostMapping("/check-valid")
    public String isValidDeleteCodes(@RequestParam String email) {
        VerificationCode storedValid = emailService.getVerificationCode(email);

        if (storedValid == null) {
            return "존재하지 않는 인증 코드입니다.";  // 예외 처리를 추가할 수 있음
        }

        if (!storedValid.isVerified()) {
            return "인증이 완료되지 않았습니다."; // 인증이 완료되지 않았을 경우
        }

        // 인증이 완료되었을 경우
        emailService.removeVerificationCode(email);
        return "인증 완료. 삭제 가능."; // 더 명확한 메시지
    }


    // 저장된 인증코드들 확인용
    @GetMapping("/verification-codes")
    public ResponseEntity<Map<String, VerificationCode>> getAllVerificationCodes() {
        return ResponseEntity.ok(emailService.getAllVerificationCodes());
    }



    // 초대 이메일 발송 API
//    @PostMapping("/invite")
//    public ResponseEntity<String> sendHtmlVerificationEmails(@RequestBody Map<String, Object> request) {
//        try {
//            // Map에서 데이터 추출
//            List<String> emails = (List<String>) request.get("emails");
//            String userName = (String) request.get("userName");
//            String projectName = (String) request.get("projectName");
//            String dynamicLink = (String) request.get("dynamicLink");
//
//            // 이메일 서비스 메서드 호출
//            emailService.sendInviteEmails(emails, userName, projectName, dynamicLink);
//
//            return ResponseEntity.ok ("HTML 이메일 전송 완료.");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("템플릿 파일 읽기 실패: " + e.getMessage());
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 데이터 형식이 올바르지 않습니다.");
//        }
//    }


    @PostMapping("/invite")
    public ResponseEntity<String> sendHtmlVerificationEmails(@RequestBody Map<String, Object> request) {
        try {
            // Map에서 데이터 추출
            List<String> emails = (List<String>) request.get("emails");
            String userName = (String) request.get("userName");
            String projectName = (String) request.get("projectName");
            String dynamicLink = (String) request.get("dynamicLink");

            // 이메일을 병렬로 처리
            emailService.sendInviteEmails(emails, userName, projectName, dynamicLink);

            return ResponseEntity.ok("HTML 이메일 전송 완료.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/PW")
    public ResponseEntity<String> sendPWEmails(@RequestBody Map<String, Object> request) {
        try {
            // Map에서 데이터 추출
            String emails = request.get("email").toString();
            String userName = (String) request.get("userName");
            String dynamicLink = (String) request.get("dynamicLink");
            String token = (String) request.get("token");

            // 이메일을 병렬로 처리
            emailService.sendPWEmail(emails, userName, dynamicLink, token);

            return ResponseEntity.ok("PW 이메일 전송 완료.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
        }
    }



}

