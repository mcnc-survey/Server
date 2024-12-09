package api.mcnc.surveyadminservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultToken;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-08 오후 7:31
 */
@Configuration
public class VaultConfig {
  @Value("${spring.cloud.vault.token}")
  private String vaultToken;
  @Value("${spring.cloud.vault.scheme}")
  private String vaultScheme;
  @Value("${spring.cloud.vault.host}")
  private String vaultHost;
  @Value("${spring.cloud.vault.port}")
  private int vaultPort;

  @Bean
  public VaultTemplate vaultTemplate(){
    VaultEndpoint endpoint = VaultEndpoint.create(vaultHost, vaultPort);
    endpoint.setScheme(vaultScheme);

    VaultTemplate template = new VaultTemplate(endpoint, ()-> VaultToken.of(vaultToken));
    return template;
  }
}
