package api.mcnc.surveyadminservice.repository.history;

import api.mcnc.surveyadminservice.domain.AdminHistory;
import api.mcnc.surveyadminservice.entity.admin.AdminHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:21
 */
@Repository
@RequiredArgsConstructor
public class LogAdminHistoryRepository {

  private final LogAdminHistoryJpaRepository adminHistoryJpaRepository;


  public void log(AdminHistory adminHistory) {
    adminHistoryJpaRepository.save(AdminHistoryEntity.from(adminHistory));
  }
}
