package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.authentication.auth.EmailUserInfo;
import api.mcnc.surveyrespondentservice.common.enums.SuccessCode;
import api.mcnc.surveyrespondentservice.common.result.Api;
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

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 11:19
 */
@RestController
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthUseCase oauthUseCase;
  private final RegisterUseCase registerUseCase;

  @PostMapping("/auth/sign")
  public Api<Token> oauthEmail(@RequestBody @Valid EmailUserInfo emailUser, @RequestParam("t") String t) {
    String surveyId = decodeSurveyId(t);
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

