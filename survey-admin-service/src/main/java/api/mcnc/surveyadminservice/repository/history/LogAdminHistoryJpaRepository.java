package api.mcnc.surveyadminservice.repository.history;

import api.mcnc.surveyadminservice.entity.admin.AdminHistoryEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:21
 */
public interface LogAdminHistoryJpaRepository extends CrudRepository<AdminHistoryEntity, String> {
}
