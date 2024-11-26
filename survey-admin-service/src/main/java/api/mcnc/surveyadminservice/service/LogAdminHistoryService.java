package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.domain.AdminHistory;
import api.mcnc.surveyadminservice.repository.history.LogAdminHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:17
 */
@Service
@RequiredArgsConstructor
public class LogAdminHistoryService {

  private final LogAdminHistoryRepository logAdminHistoryRepository;

  public void log(String userId, String userRole, String clientIp, String reqMethod, String reqUrl, String reqHeader, String reqPayload) {
    AdminHistory adminHistory =  AdminHistory.builder()
      .adminId(userId)
      .adminRole(userRole)
      .clientIp(clientIp)
      .reqMethod(reqMethod)
      .reqUrl(reqUrl)
      .reqHeader(reqHeader)
      .reqPayload(reqPayload)
      .build();
    logAdminHistoryRepository.log(adminHistory);
  }
}
