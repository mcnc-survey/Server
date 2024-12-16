package api.mcnc.surveyadminservice.auth.handler;

import api.mcnc.surveyadminservice.auth.dto.model.AdminPrincipalDetails;
import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private static final String URI = "http://localhost:8081/aaaa.html";
//  private static final String URI = "https://mcnc-survey-client.vercel.app/";
  private static final String QUERY_PARAM_NAME = "accessToken";
  private static final String COOKIE_HEADER_NAME = "Set-Cookie";
  private final TokenProvider tokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    AdminPrincipalDetails principal = (AdminPrincipalDetails) authentication.getPrincipal();
    Admin admin = principal.admin();

    Token token = tokenProvider.issue(admin);
    String accessToken = token.accessToken();
    String refreshToken = token.refreshToken();

    String cookie = CookieUtils.setRefreshTokenCookie(refreshToken);

    response.addHeader(COOKIE_HEADER_NAME, cookie);
    response.sendRedirect(makeRedirectUrl(accessToken));
  }

  public String makeRedirectUrl(String accessToken) {
    return UriComponentsBuilder.fromUriString(URI)
      .queryParam(QUERY_PARAM_NAME, accessToken)
      .build().toUriString();
  }
}
