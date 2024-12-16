package api.mcnc.surveyadminservice.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 12:57
 */
public interface TokenRepository extends CrudRepository<TokenEntity, String>{
  Optional<TokenEntity> findByAccessToken(String accessToken);
}
