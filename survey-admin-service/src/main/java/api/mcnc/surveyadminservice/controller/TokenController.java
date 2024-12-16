package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.enums.SuccessCode;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.result.Api;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.controller.request.TokenReissueRequest;
import api.mcnc.surveyadminservice.controller.response.TokenResponse;
import api.mcnc.surveyadminservice.domain.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
 * @since :2024-12-15 오후 11:58
 */
@RestController
@RequiredArgsConstructor
public class TokenController {

  private final TokenProvider tokenProvider;

  @PostMapping("/token/reissue")
  public Api<TokenResponse> reissue(@RequestBody @Valid TokenReissueRequest tokenRequest, HttpServletRequest request, HttpServletResponse response) {
    String accessToken = tokenRequest.accessToken();
    String refreshToken = extractRefreshTokenInCookies(request);

    Token token = tokenProvider.reissueAccessToken(accessToken, refreshToken);
    String reIssueAccessToken = token.accessToken();
    String reIssueRefreshToken = token.refreshToken();

    CookieUtils.setCookie(reIssueRefreshToken, response);

    return Api.ok(SuccessCode.SUCCESS, new TokenResponse(reIssueAccessToken));
  }

  /**
   * 쿠키에서 refreshToken 추출
   * @param request {@link HttpServletRequest}
   * @return refreshToken
   */
  private String extractRefreshTokenInCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refreshToken")) {
        return cookie.getValue();
      }
    }
    throw new AdminException(AdminErrorCode.INVALID_REQUEST, "refreshToken 값이 null일 수 없습니다.");
  }
}
