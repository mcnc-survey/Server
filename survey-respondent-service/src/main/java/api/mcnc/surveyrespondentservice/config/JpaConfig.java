package api.mcnc.surveyrespondentservice.config;

import api.mcnc.surveyrespondentservice.entity.EntityModule;
import api.mcnc.surveyrespondentservice.repository.RepositoryModule;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 10:08
 */
@Configuration
@EntityScan(basePackageClasses = EntityModule.class)
@EnableJpaRepositories(basePackageClasses = RepositoryModule.class)
@EnableConfigurationProperties
public class JpaConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource datasource() {
    return DataSourceBuilder.create().build();
  }

}
