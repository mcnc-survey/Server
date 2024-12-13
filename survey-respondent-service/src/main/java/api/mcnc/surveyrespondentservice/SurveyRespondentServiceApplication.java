package api.mcnc.surveyrespondentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SurveyRespondentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SurveyRespondentServiceApplication.class, args);
  }

}
