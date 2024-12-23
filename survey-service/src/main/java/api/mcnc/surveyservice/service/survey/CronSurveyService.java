package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.notification.NotificationClientService;
import api.mcnc.surveyservice.client.notification.Request;
import api.mcnc.surveyservice.client.notification.Type;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.entity.survey.SurveyEntity;
import api.mcnc.surveyservice.repository.survey.UpdateSurveyStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static api.mcnc.surveyservice.client.notification.Type.SURVEY_END;
import static api.mcnc.surveyservice.client.notification.Type.SURVEY_START;
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

  private final UpdateSurveyStatusRepository updateSurveyStatusRepository;
  private final NotificationClientService notificationClientService;

  @Scheduled(cron = "0 * * * * *") // 1분 마다
  @Transactional
  public void updateSurveyStatus() {
    LocalDateTime now = LocalDateTime.now();
    List<SurveyEntity> startEntities = updateSurveyStatusRepository.updateSurveyStatusToStart(ON, WAIT, now);
    List<SurveyEntity> endEntities = updateSurveyStatusRepository.updateSurveyStatusToEnd(END, ON, now);

    // 시작 되었습니다 알림
    if (!startEntities.isEmpty()) {
      String adminId = startEntities.get(0).getAdminId();
      notification(startEntities, SURVEY_START, adminId);
    }
    // 종료 되었습니다 알림
    if (!endEntities.isEmpty()) {
      String adminId = endEntities.get(0).getAdminId();
      notification(endEntities, SURVEY_END, adminId);
    }

  }

  private void notification(List<SurveyEntity> entities, Type type, String adminId) {
    entities.stream()
      .map(entity -> Request.of(entity.getId(), entity.getTitle(), type))
      .forEach(request -> notificationClientService.publishNotification(request, adminId));
  }
}
