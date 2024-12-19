package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.auth.vault.Vault;
import api.mcnc.surveyadminservice.client.EmailClientService;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.request.PasswordChangeRequest;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.controller.response.EmailVerifyCheckResponse;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import api.mcnc.surveyadminservice.service.response.AdminSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static api.mcnc.surveyadminservice.common.enums.AdminErrorCode.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-12 오후 2:32
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final EmailClientService emailClientService;
  private final Vault vaultProvider;

  /**
   * 이메일 중복 검사
   *
   * @param email String
   * @return {@link EmailDuplicateCheckResponse}
   */
  public EmailDuplicateCheckResponse checkEmailDuplicate(String email) {
    Optional<Admin> isExist = adminRepository.getByEmailAdmin(email);
    boolean present = isExist.isPresent();
    return EmailDuplicateCheckResponse.isDuplicated(email, present);
  }

  /**
   * 이메일 중복 검사 후 인증 코드 전송
   *
   * @param email String
   */
  public void checkEmailDuplicateAndSendEmail(String email) {
    String encryptedEmail = vaultProvider.encrypt(email);
    EmailDuplicateCheckResponse checkDupResult = checkEmailDuplicate(encryptedEmail);
    if (checkDupResult.getIsDuplicated()) {
      throw new AdminException(ALREADY_EXIST);
    }
    emailClientService.requestVerificationCode(email);
  }

  /**
   * 이메일 인증 코드 검증
   *
   * @param email String
   * @param code  String
   * @return {@link EmailVerifyCheckResponse}
   */
  public EmailVerifyCheckResponse sendEmailVerificationCode(String email, String code) {
    boolean isValid = emailClientService.verifyCode(email, code);
    return new EmailVerifyCheckResponse(isValid);
  }

  public boolean isValidEmail(String email) {
    return emailClientService.isValidEmail(email);
  }

  /**
   * 회원가입
   *
   * @param request {@link AdminSignUpRequest}
   */
  public void signUp(AdminSignUpRequest request) {
    adminRepository.getByEmailAdmin(request.getEmail())
      .ifPresent(admin -> {
        throw new AdminException(ALREADY_EXIST);
      });
    Admin admin = Admin.from(request);
    Admin registered = adminRepository.registerWithEmail(admin);
  }

  /**
   * 로그인
   *
   * @param request {@link AdminSignInRequest}
   * @return {@link Token}
   */
  public AdminSignInResponse signIn(AdminSignInRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    // 해당 email로 회원 가입 여부 확인
    Admin admin = adminRepository.getByEmailAdmin(email)
      .orElseThrow(() -> new AdminException(NOT_FOUND));

    // 비밀번호 검증
    boolean matches = passwordEncoder.matches(password, admin.password());

    // 일치하면 토큰 생성
    if (matches) {
      Token token = tokenProvider.issue(admin);
      return AdminSignInResponse.of(admin.name(), token);
    }
    // 불 일치 에러 반환
    throw new AdminException(MISS_MATCH_ADMIN_ACCOUNT);
  }

  /**
   * 로그아웃 <br/>
   * 토큰 받아서 제거
   * @param accessToken String
   */
  public void signOut(String accessToken) {
    tokenProvider.expireToken(accessToken);
  }

  /**
   * 비밀번호 변경
   *
   * @param request {@link PasswordChangeRequest}
   */
  public void changePassword(PasswordChangeRequest request) {
    String newPassword = request.getNewPassword();
    String token = request.getToken();

    String adminId = tokenProvider.validateTokenAndExtractAdminId(token)
      .orElseThrow(() -> new AdminException(TokenErrorCode.INVALID_TOKEN));

    adminRepository.changePassword(adminId, newPassword);
  }

  public String sendPasswordChangeEmail(String email) {
    String encryptedEmail = vaultProvider.encrypt(email);
    Admin admin = adminRepository.getByEmailAdmin(encryptedEmail)
      .orElseThrow(() -> new AdminException(NOT_FOUND, "가입되지 않은 이메일입니다."));
    final long EXPIRE_TIME = 1000 * 60 * 5;
    String token = tokenProvider.generateAccessToken(admin, EXPIRE_TIME);
    return emailClientService.sendPWEmails(email, admin.name(), token);
  }
}
