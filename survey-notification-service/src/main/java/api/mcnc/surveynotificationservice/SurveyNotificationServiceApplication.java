package api.mcnc.surveynotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SurveyNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyNotificationServiceApplication.class, args);
	}

}
