package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.authentication.auth.EmailUserInfo;
import api.mcnc.surveyrespondentservice.client.email.EmailValidateUseCase;
import api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode;
import api.mcnc.surveyrespondentservice.common.enums.SuccessCode;
import api.mcnc.surveyrespondentservice.common.exception.custom.RespondentException;
import api.mcnc.surveyrespondentservice.common.result.Api;
import api.mcnc.surveyrespondentservice.controller.request.EmailVerifyCheckRequest;
import api.mcnc.surveyrespondentservice.controller.request.EmailVerifyRequest;
import api.mcnc.surveyrespondentservice.controller.response.EmailVerifyCheckResponse;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.service.auth.OAuthUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RegisterUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

import static api.mcnc.surveyrespondentservice.common.constant.ProviderConstant.DEFAULT_PROVIDER;
import static api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode.NOT_VERIFIED_EMAIL;

/**
 * 인증 컨트롤러
 *
 * @author :유희준
 * @since :2024-11-20 오후 11:19
 */
@RestController
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthUseCase oauthUseCase;
  private final RegisterUseCase registerUseCase;
  private final EmailValidateUseCase emailValidateUseCase;

  @PostMapping("/auth/email-verify")
  public Api<Void> emailVerify(@RequestBody @Valid EmailVerifyRequest request) {
    emailValidateUseCase.requestVerificationCode(request.email());
    return Api.ok(SuccessCode.SUCCESS, null);
  }

  @PostMapping("/auth/email-verify/check")
  public Api<EmailVerifyCheckResponse> emailVerifyCheck(@RequestBody @Valid EmailVerifyCheckRequest request) {
    EmailVerifyCheckResponse response = emailValidateUseCase.verifyCode(request.email(), request.code());
    return Api.ok(SuccessCode.SUCCESS, response);
  }

  @PostMapping("/auth/sign")
  public Api<Token> oauthEmail(@RequestBody @Valid EmailUserInfo emailUser, @RequestParam("t") String t) {
    String surveyId = decodeSurveyId(t);
    // 인증된 이메일만 허용
    boolean validEmail = emailValidateUseCase.isValidEmail(emailUser.email());
    // 인증되지 않은 이메일이면 에러
    if(!validEmail) {
      throw new RespondentException(NOT_VERIFIED_EMAIL);
    }
    AuthenticatedRespondent emailUserInfo = AuthenticatedRespondent.of(emailUser, DEFAULT_PROVIDER);
    Token token = registerUseCase.registerRespondent(emailUserInfo, surveyId);
    return Api.ok(SuccessCode.SUCCESS, token);
  }

  @GetMapping("/auth/{provider}/redirection")
  public void socialLogin(
    @PathVariable(name = "provider") String provider,
    @RequestParam("code") String code,
    @RequestParam("state") String state,
    @RequestParam(value = "surveyId", required = false) String surveyId,
    HttpServletResponse response
  ) throws IOException {
    String extractId = extractSurveyId(state);
    if("naver".equals(provider)) {
      extractId = surveyId;
    }
    extractId = decodeSurveyId(extractId);
    AuthenticatedRespondent authenticatedSocialUser = oauthUseCase.getAuthenticatedSocialUser(provider, code);
    Token token = registerUseCase.registerRespondent(authenticatedSocialUser, extractId);

    response.sendRedirect("https://mcnc-survey-client.vercel.app/mobile?token=" + token.accessToken());
  }

  private String extractSurveyId(String state) {
    final String findText = "surveyId=";
    int index = state.indexOf(findText);
    return state.substring(index + findText.length());
  }

  private String decodeSurveyId(String t) {
    byte[] decode = Base64.getDecoder().decode(t);
    return new String(decode);
  }

}

