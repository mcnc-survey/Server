package api.mcnc.surveyrespondentservice.config;

import api.mcnc.surveyrespondentservice.authentication.auth.CustomOAuth2AuthorizationRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오전 10:06
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
      .requestMatchers("/error", "/favicon.ico");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
    DefaultOAuth2AuthorizationRequestResolver defaultResolver =
      new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

    CustomOAuth2AuthorizationRequestResolver customResolver = new CustomOAuth2AuthorizationRequestResolver(defaultResolver);

    http
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(request -> request.anyRequest().permitAll())
      .oauth2Login(oauth2 -> oauth2
        .authorizationEndpoint(auth -> auth.authorizationRequestResolver(customResolver))
      )

    ;
    return http.build();
  }

}
