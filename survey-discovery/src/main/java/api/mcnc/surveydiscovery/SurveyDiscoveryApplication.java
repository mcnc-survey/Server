package api.mcnc.surveydiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SurveyDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(SurveyDiscoveryApplication.class, args);
  }

}
