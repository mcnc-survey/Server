package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import api.mcnc.surveyservice.repository.survey.SurveyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static api.mcnc.surveyservice.entity.survey.SurveyStatus.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 1:20
 */
@Service
@RequiredArgsConstructor
public class CronSurveyService {

  private final SurveyJpaRepository surveyRepository;

  @Scheduled(cron = "0 * * * * *") // 1분 마다
  @Transactional
  public void updateSurveyStatus() {
    try {
      LocalDateTime now = LocalDateTime.now();
      surveyRepository.updateSurveyStatusToStart(ON, WAIT, now);
      surveyRepository.updateSurveyStatusToEnd(END, ON, now);
    } catch (Exception e) {
      e.printStackTrace();  // 예외 처리 및 로깅
    }
  }
}
