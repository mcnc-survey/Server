package api.mcnc.surveyresponsegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SurveyResponseGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(SurveyResponseGatewayApplication.class, args);
  }

}
