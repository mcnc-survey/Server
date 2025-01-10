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
import java.sql.Struct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author 차익현
 */
@Slf4j
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class VerificationController {

    @Autowired
    private EmailService emailService;

    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();


    /**
     * 인증 코드 요청 API
     *
     * @param email 이메일
     * @return 인증 코드 발송 완료
     */
    @GetMapping("/request-code")
    public String requestVerificationCode(@RequestParam String email) throws MessagingException, IOException {
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendSingleVerificationEmail(email, verificationCode);
        emailService.saveVerificationCode(email, verificationCode);

        return "인증 코드 발송 완료.";
    }

    /**
     * 다중 인증 코드 요청 PI
     * @param emails 이메일 리스트
     * @return 모든 이메일로 인증 코드 발송
     */
    @PostMapping("/multi-request")
    public ResponseEntity<String> requestVerificationCode(@RequestBody List<String> emails) {
        // 이메일 목록이 비어 있으면 오류 반환
        if (CollectionUtils.isEmpty(emails)) {
            return ResponseEntity.badRequest().body("이메일 목록이 비어 있습니다.");
        }

        try {
            // 병렬 스트림을 사용해 이메일 처리 성능 개선
            emails.parallelStream()
                    .forEach(email -> {
                        try {
                            // 인덱스 기반 고유 인증 코드 생성
                            int index = emails.indexOf(email) + 1;
                            String verificationCode = emailService.generateMultiVerificationCode(index);

                            // 이메일 발송 및 코드 저장
                            emailService.sendSingleVerificationEmail(email, verificationCode);
                            emailService.saveVerificationCode(email, verificationCode);

                        } catch (Exception e) {
                            log.error("이메일 처리 중 오류: {}", email, e);
                        }
                    });


            return ResponseEntity.ok("모든 이메일로 인증 코드 발송");
        } catch (Exception e) {
            log.error("다중 이메일 검증 중 예상치 못한 오류", e);
            return ResponseEntity.internalServerError().body("예상치 못한 오류 발생");
        }
    }


    /**
     * 인증 코드 검증 API
     *
     * @param email 이메일
     * @param code 코드
     * @return 인증성공 true
     */
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

    /**
     * 인증 검증이 됐는지 검증 API
     *
     * @param email 이메일
     * @return 인증 검증 완료 true
     */
    @PostMapping("/check-valid")
    public boolean isValidDeleteCodes(@RequestParam String email) {
        VerificationCode storedValid = emailService.getVerificationCode(email);

        if (storedValid == null) {
            return false;  // 예외 처리를 추가할 수 있음
        }

        if (!storedValid.isVerified()) {
            return false; // 인증이 완료되지 않았을 경우
        }

        // 인증이 완료되었을 경우
        emailService.removeVerificationCode(email);
        return true; // 더 명확한 메시지
    }


    /**
     * 저장된 인증코드들 확인용 test API
     * @return 저장된 모든 인증코드
     */
    @GetMapping("/verification-codes")
    public ResponseEntity<Map<String, VerificationCode>> getAllVerificationCodes() {
        return ResponseEntity.ok(emailService.getAllVerificationCodes());
    }


    /**
     * 초대 html 전송 API
     *
     * @param request List<String> toEmails, String projectName, String dynamicLink
     * @return HTML 이메일 전송 완료
     */
    @PostMapping("/invite")
    public ResponseEntity<String> sendHtmlVerificationEmails(@RequestBody Map<String, Object> request) {
        try {
            // Map에서 데이터 추출
            List<String> emails = (List<String>) request.get("emails");
            String projectName = (String) request.get("projectName");
            String dynamicLink = (String) request.get("dynamicLink");

            // 이메일을 병렬로 처리
            emailService.sendInviteEmails(emails, projectName, dynamicLink);

            return ResponseEntity.ok("HTML 이메일 전송 완료.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage());
        }
    }

    /**
     * 비밀번호 재설정 html 전송 API
     *
     * @param request String toEmail, String userName, String dynamicLink, String token
     * @return PW 이메일 전송 완료
     */
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

