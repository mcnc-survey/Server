package api.mcnc.surveyadminservice.repository.history;

import api.mcnc.surveyadminservice.entity.admin.AdminHistoryEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 8:05
 */
public interface AdminHistoryJpaRepository extends CrudRepository<AdminHistoryEntity, String> {
}
