package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import api.mcnc.surveyadminservice.service.response.SignInResponse;
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
  public SignInResponse signIn(AdminSignInRequest request) {
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
      return SignInResponse.of(admin.name(), token);
    }
    // 불 일치 에러 반환
    throw new AdminException(MISS_MATCH_ADMIN_ACCOUNT);
  }


  public void signOut(String accessToken) {
    tokenProvider.expireToken(accessToken);
  }
}
