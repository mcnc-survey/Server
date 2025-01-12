package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.controller.request.*;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.controller.response.EmailVerifyCheckResponse;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.service.AuthService;
import api.mcnc.surveyadminservice.service.response.AdminSignInResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * please explain class!
 *
 * @author :유희준
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

  @PostMapping("/auth/email-verify")
  public Api<Void> emailVerify(@RequestBody @Valid EmailVerifyRequest request) {
    authService.checkEmailDuplicateAndSendEmail(request.email());
    return Api.ok(SuccessCode.SUCCESS, null);
  }

  @PostMapping("/auth/email-verify/check")
  public Api<EmailVerifyCheckResponse> emailVerifyCheck(@RequestBody @Valid EmailVerifyCheckRequest request) {
    EmailVerifyCheckResponse response = authService.sendEmailVerificationCode(request.email(), request.code());
    return Api.ok(SuccessCode.SUCCESS, response);
  }

  @PostMapping("/auth/sign-up")
  public Api<Void> signUpAdmin(@RequestBody @Valid AdminSignUpRequest request) {
    // 인증된 이메일만 회원가입 허용
    boolean isValidEmail = authService.isValidEmail(request.getEmail());

    if (!isValidEmail) {
      throw new AdminException(AdminErrorCode.NOT_VERIFIED_EMAIL);
    }

    authService.signUp(request);
    return Api.ok(SuccessCode.RESPONSE_CREATE_SUCCESS, null);
  }

  @PostMapping("/auth/sign-in")
  public Api<TokenResponse> signInAdmin(@RequestBody @Valid AdminSignInRequest request, HttpServletResponse response) {
    AdminSignInResponse adminSignInResponse = authService.signIn(request);
    CookieUtils.setCookie(adminSignInResponse.refreshToken(), response);
    return Api.ok(SuccessCode.SUCCESS, TokenResponse.of(adminSignInResponse.userName(), adminSignInResponse.accessToken()));
  }

  @DeleteMapping("/auth/sign-out")
  public Api<Void> signOutAdmin(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = extractAccessTokenInHeader(request);
    authService.signOut(accessToken);
    CookieUtils.deleteCookie(response);
    return Api.ok(SuccessCode.SUCCESS, null);
  }

  @GetMapping("/auth/password-change")
  public Api<String> sendPasswordChangeEmail(@RequestParam("email") String email) {
    String resultMessage = authService.sendPasswordChangeEmail(email);
    return Api.ok(SuccessCode.SUCCESS, resultMessage);
  }

  @PostMapping("/auth/password-change")
  public Api<Void> resetPassword(@RequestBody @Valid PasswordChangeRequest request) {
    authService.changePassword(request);
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
