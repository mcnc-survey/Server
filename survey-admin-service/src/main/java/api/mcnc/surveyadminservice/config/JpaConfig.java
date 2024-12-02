package api.mcnc.surveyadminservice.config;

import api.mcnc.surveyadminservice.entity.EntityModule;
import api.mcnc.surveyadminservice.repository.RepositoryModule;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 12:49
 */
@Configuration
@EntityScan(basePackageClasses = EntityModule.class)
@EnableJpaRepositories(basePackageClasses = RepositoryModule.class)
public class JpaConfig {
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource datasource() {
    return DataSourceBuilder.create().build();
  }
}
