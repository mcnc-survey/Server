package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.common.enums.SuccessCode;
import api.mcnc.surveyrespondentservice.common.result.Api;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedUser;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.service.auth.OAuthUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RegisterUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RespondentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

  @GetMapping("/{provider}/redirection")
  public Api<Void> oauthRedirect(@PathVariable("provider") String provider, @RequestParam("code") String code, @RequestParam("state") String surveyId, HttpServletResponse response) throws IOException {
//    oauth 인증된 유저 정보 반환
    AuthenticatedUser authenticatedUser = oauthUseCase.getAuthenticatedSocialUser(provider, code);

//    인증된 유저 정보로 토큰 생성
    Token token = registerUseCase.registerRespondent(authenticatedUser, surveyId);

    // 성공시 redirection
    response.setHeader("accessToken", token.accessToken());
    response.sendRedirect("http://localhost:9000/respondent/index");
    return Api.ok(SuccessCode.SUCCESS, null);
  }

}

