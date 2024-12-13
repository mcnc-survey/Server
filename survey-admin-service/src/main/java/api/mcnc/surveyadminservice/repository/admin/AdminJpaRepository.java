package api.mcnc.surveyadminservice.repository.admin;

import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 2:26
 */
public interface AdminJpaRepository extends CrudRepository<AdminEntity, String>{

  Optional<AdminEntity> findByEmailAndProvider(String email, String provider);

}
