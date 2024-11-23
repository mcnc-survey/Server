package api.mcnc.surveyrespondentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오전 10:06
 */
@Configuration
public class OAuth2Config {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .oauth2Login(AbstractHttpConfigurer::disable)
      .oauth2Client(AbstractHttpConfigurer::disable)

      ;
    return http.build();
  }

}
