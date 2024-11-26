package api.mcnc.surveyadminservice.config;

import api.mcnc.surveyadminservice.filter.UserHistoryLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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

  private final UserHistoryLoggingFilter userHistoryLoggingFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .sessionManagement(AbstractHttpConfigurer::disable)
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .authorizeHttpRequests(request ->
        request
          .requestMatchers("/api/sign-in", "/api/sign-up").permitAll()
          .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> {
        oauth2.failureUrl("/login?error=true");
//        oauth2
      })

//      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(userHistoryLoggingFilter, UsernamePasswordAuthenticationFilter.class)
    ;
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration cors = new CorsConfiguration();
      cors.setAllowedMethods(Collections.singletonList("*"));
      cors.setAllowedHeaders(Collections.singletonList("*"));
      cors.setAllowedOriginPatterns(Collections.singletonList("*"));
      cors.setAllowCredentials(true);
      return cors;
    };
  }

}
