package api.mcnc.surveyresponseservice;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 10:11
 */
@TestConfiguration
public class RestDocsConfig {

  @Bean
  public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
    return configurer -> configurer.operationPreprocessors()
      .withRequestDefaults(prettyPrint())
      .withResponseDefaults(prettyPrint());
  }

}