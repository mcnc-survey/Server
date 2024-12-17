package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.request.EmailDuplicateCheckRequest;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.service.AuthService;
import api.mcnc.surveyadminservice.service.response.SignInResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-12 오전 10:46
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/email-duplicate-check")
  public Api<EmailDuplicateCheckResponse> checkEmailDuplicate(@RequestBody @Valid EmailDuplicateCheckRequest request) {
    EmailDuplicateCheckResponse result = authService.checkEmailDuplicate(request.getEmail());
    return Api.ok(SuccessCode.SUCCESS, result);
  }

  @PostMapping("/auth/sign-up")
  public Api<Void> signUpAdmin(@RequestBody @Valid AdminSignUpRequest request) {
    authService.signUp(request);
    return Api.ok(SuccessCode.RESPONSE_CREATE_SUCCESS, null);
  }


  @PostMapping("/auth/sign-in")
  public Api<TokenResponse> signInAdmin(@RequestBody @Valid AdminSignInRequest request, HttpServletResponse response) {
    SignInResponse signInResponse = authService.signIn(request);
    CookieUtils.setCookie(signInResponse.refreshToken(), response);
    return Api.ok(SuccessCode.SUCCESS, TokenResponse.of(signInResponse.userName(), signInResponse.accessToken()));
  }

  @DeleteMapping("/auth/sign-out")
  public Api<Void> signOutAdmin(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = extractAccessTokenInHeader(request);
    CookieUtils.deleteCookie(response);
    authService.signOut(accessToken);
    return Api.ok(SuccessCode.SUCCESS, null);
  }

  private String extractAccessTokenInHeader(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    final String AUTHORIZATION_PREFIX = "Bearer ";
    if (authorization == null || !authorization.startsWith(AUTHORIZATION_PREFIX)) {
      throw new AdminException(TokenErrorCode.INVALID_TOKEN);
    }

    return authorization.substring(AUTHORIZATION_PREFIX.length());
  }

}
