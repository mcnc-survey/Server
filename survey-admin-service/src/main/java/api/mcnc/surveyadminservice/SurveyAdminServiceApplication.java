package api.mcnc.surveyadminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SurveyAdminServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SurveyAdminServiceApplication.class, args);
  }

}
