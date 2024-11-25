package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.domain.AdminHistory;
import api.mcnc.surveyadminservice.repository.history.AdminHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 8:03
 */
@Service
@RequiredArgsConstructor
public class LogAdminHistoryService {

  private final AdminHistoryRepository adminHistoryRepository;

  public void log(String aminId, String adminRole, String clientIp, String reqMethod, String reqUrl, String reqHeader, String reqPayload) {
    AdminHistory history = AdminHistory.builder()
      .adminId(aminId)
      .adminRole(adminRole)
      .clientIp(clientIp)
      .reqMethod(reqMethod)
      .reqUrl(reqUrl)
      .reqHeader(reqHeader)
      .reqPayload(reqPayload)
      .build();

    adminHistoryRepository.log(history);
  }
}
