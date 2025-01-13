package api.mcnc.surveyservice.client.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-23 오후 8:22
 */
@Service
@RequiredArgsConstructor
public class AdminServiceClientService {
  private final AdminServiceClient adminServiceClient;

  @Cacheable(cacheNames = "validAdmin", key = "'admin:exist:id:' + #adminId", cacheManager = "surveyCacheManager")
  public boolean isExistAdmin(String adminId) {
    return adminServiceClient.adminValidation(adminId);
  }
}
