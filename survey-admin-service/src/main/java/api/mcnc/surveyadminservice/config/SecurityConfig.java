package api.mcnc.surveyadminservice.config;

import api.mcnc.surveyadminservice.auth.handler.OAuth2FailureHandler;
import api.mcnc.surveyadminservice.auth.handler.OAuth2SuccessHandler;
import api.mcnc.surveyadminservice.auth.service.CustomOAuth2UserService;
import api.mcnc.surveyadminservice.filter.UserHistoryLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 1:47
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final OAuth2FailureHandler oAuth2FailureHandler;
  private final UserHistoryLoggingFilter userHistoryLoggingFilter;
  private final CustomOAuth2UserService userService;

  public final String[] ALLOW_LIST = {
    "/login/oauth2/code/**",
    "/auth/**",
    "/token/**"
  };


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(request -> request.requestMatchers(ALLOW_LIST).permitAll().anyRequest().authenticated())
      .oauth2Login(oauth2 -> {
        oauth2.userInfoEndpoint(c -> c.userService(userService));
        oauth2.failureHandler(oAuth2FailureHandler);
        oauth2.successHandler(oAuth2SuccessHandler);
      })
      .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
      .addFilterAfter(userHistoryLoggingFilter, UsernamePasswordAuthenticationFilter.class)
    ;
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration config = new CorsConfiguration();
      config.setMaxAge(3600L);
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.addAllowedOriginPattern("*");
      config.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", config);
      return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
