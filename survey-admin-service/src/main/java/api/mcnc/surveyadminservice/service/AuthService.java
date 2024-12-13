package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.response.AdminSignUpResponse;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

  public AdminSignUpResponse signUp(AdminSignUpRequest request) {
    Admin admin = Admin.from(request);
    Optional<Admin> isExist = adminRepository.getByEmailAndProvider(admin.email(), admin.provider());

    // email 중복 회원 가입 허용 안함
    if(isExist.isPresent()) {
      throw new AdminException(AdminErrorCode.ALREADY_EXIST);
    }

    Admin registered = adminRepository.registerWithEmail(admin);
    return registered.toSignUpResponse();
  }

  public Token signIn(AdminSignInRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    Admin admin = adminRepository.getByEmailAdmin(email);

    boolean matches = passwordEncoder.matches(admin.password(), password);

    if (matches) {
//      return tokenProvider.generateAccessToken(admin);
    }
    return new Token("asdf", "sdf");
  }



}
