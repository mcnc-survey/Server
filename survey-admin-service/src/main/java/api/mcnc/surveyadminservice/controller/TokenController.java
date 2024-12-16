package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.domain.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-15 오후 11:58
 */
@RestController
@RequiredArgsConstructor
public class TokenController {

  private final TokenProvider tokenProvider;

  @PostMapping("/token/reissue")
  public Api<TokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = extractAccessTokenInHeader(request);
    String refreshToken = CookieUtils.extractRefreshTokenInCookies(request);
    Token token = tokenProvider.reissueAccessToken(accessToken, refreshToken);
    String reIssueAccessToken = token.accessToken();
    String reIssueRefreshToken = token.refreshToken();

    CookieUtils.setCookie(reIssueRefreshToken, response);

    return Api.ok(SuccessCode.SUCCESS, TokenResponse.of(reIssueAccessToken));
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
