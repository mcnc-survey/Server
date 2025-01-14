package api.mcnc.surveyresponseservice.repository.delete;

import api.mcnc.surveyresponseservice.entity.delete.DeleteQueueEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-19 오후 8:11
 */
public interface DeleteQueueJpaRepository extends ListCrudRepository<DeleteQueueEntity, Long> {
}
