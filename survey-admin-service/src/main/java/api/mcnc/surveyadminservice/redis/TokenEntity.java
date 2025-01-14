package api.mcnc.surveyadminservice.redis;

import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.entity.audit.MutableBaseEntity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * redis 토큰
 *
 * @author :유희준
 * @since :2024-11-26 오후 12:40
 */
@Builder(access = AccessLevel.PRIVATE)
@RedisHash(value = "tokens", timeToLive = 60 * 60 * 24 * 14) // 2주
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity {
  @Id
  private String id;
  @Getter
  private String refreshToken;
  @Indexed
  private String accessToken;

  public static TokenEntity fromDomain(Token token) {
    return TokenEntity.builder()
      .id(token.id())
      .refreshToken(token.refreshToken())
      .accessToken(token.accessToken())
      .build();
  }

  public static TokenEntity of(String id, String refreshToken, String accessToken) {
    return TokenEntity.builder()
      .id(id)
      .refreshToken(refreshToken)
      .accessToken(accessToken)
      .build();
  }

  public TokenEntity updateAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public Token toToken() {
    return Token.builder()
      .id(id)
      .refreshToken(refreshToken)
      .accessToken(accessToken)
      .build();
  }

  public TokenEntity updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }
}
