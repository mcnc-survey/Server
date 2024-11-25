package api.mcnc.surveyservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:22
 */
@Service
@RequiredArgsConstructor
public class AdminServiceClientService {
  private final AdminServiceClient adminServiceClient;

  public boolean isExistAdmin(String adminId) {
    return true;
//    return adminServiceClient.adminValidation(adminId);
  }
}
