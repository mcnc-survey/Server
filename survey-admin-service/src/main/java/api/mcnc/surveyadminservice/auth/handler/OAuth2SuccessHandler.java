package api.mcnc.surveyadminservice.auth.handler;

import api.mcnc.surveyadminservice.auth.dto.model.AdminPrincipalDetails;
import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.common.utils.CookieUtils;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  @Value("${oauth2.success-redirect}")
  private String uri;
  private static final String ACCESS_TOKEN = "accessToken";
  private static final String USER_NAME = "userName";
  private final TokenProvider tokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    AdminPrincipalDetails principal = (AdminPrincipalDetails) authentication.getPrincipal();
    Admin admin = principal.admin();

    Token token = tokenProvider.issue(admin);
    String accessToken = token.accessToken();
    String refreshToken = token.refreshToken();

    CookieUtils.setCookie(refreshToken, response);

    response.sendRedirect(makeRedirectUrl(accessToken, admin.name()));
  }

  public String makeRedirectUrl(String accessToken, String userName) {
    return UriComponentsBuilder.fromUriString(uri)
      .queryParam(ACCESS_TOKEN, accessToken)
      .queryParam(USER_NAME, userName)
      .encode().toUriString();
  }
}
