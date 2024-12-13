package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.response.AdminSignUpResponse;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/sign-up")
  public Api<Void> signUpAdmin(@RequestBody @Valid AdminSignUpRequest request) {
    AdminSignUpResponse adminSignUpResponse = authService.signUp(request);
    return Api.ok(SuccessCode.RESPONSE_CREATE_SUCCESS, null);
  }

  @PostMapping("/sign-in")
  public Api<Token> signInAdmin(@RequestBody @Valid AdminSignInRequest request) {
    Token token = authService.signIn(request);
    return Api.ok(SuccessCode.SUCCESS, token);
  }

  @GetMapping("/admin")
  public String admin() {
    return "admin";
  }

}
