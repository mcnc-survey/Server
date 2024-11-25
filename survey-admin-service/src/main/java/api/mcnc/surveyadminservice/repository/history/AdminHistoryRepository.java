package api.mcnc.surveyadminservice.repository.history;

import api.mcnc.surveyadminservice.domain.AdminHistory;
import api.mcnc.surveyadminservice.entity.admin.AdminHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 8:06
 */
@Repository
@RequiredArgsConstructor
public class AdminHistoryRepository {

  private final AdminHistoryJpaRepository adminHistoryJpaRepository;

  @Transactional
  public void log(AdminHistory adminHistory) {
    adminHistoryJpaRepository.save(AdminHistoryEntity.from(adminHistory));
  }

}
