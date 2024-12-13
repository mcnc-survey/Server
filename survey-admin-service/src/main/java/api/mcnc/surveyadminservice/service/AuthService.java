package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.response.AdminSignUpResponse;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
