package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.request.EmailDuplicateCheckRequest;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    Token token = authService.signIn(request);
    TokenResponse tokenResponse = token.toResponse();

    CookieUtils.setCookie(token.refreshToken(), response);

    return Api.ok(SuccessCode.SUCCESS, tokenResponse);
  }

}
