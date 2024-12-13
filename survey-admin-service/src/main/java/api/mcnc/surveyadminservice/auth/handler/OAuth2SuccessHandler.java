package api.mcnc.surveyadminservice.auth.handler;

import api.mcnc.surveyadminservice.auth.dto.model.AdminPrincipalDetails;
import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.domain.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private static final String URI = "https://mcnc-survey-client.vercel.app";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        AdminPrincipalDetails principal = (AdminPrincipalDetails) authentication.getPrincipal();
        Admin admin = principal.admin();

        String accessToken = tokenProvider.generateAccessToken(admin);
        tokenProvider.generateRefreshToken(admin);

        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect(redirectUrl);
    }
}
