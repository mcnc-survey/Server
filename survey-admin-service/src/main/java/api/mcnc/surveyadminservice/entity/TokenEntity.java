package api.mcnc.surveyadminservice.entity;

import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.entity.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 12:40
 */
@Entity
@Table(name = "tokens")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity extends MutableBaseEntity {

  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "REFRESH_TOKEN")
  private String refreshToken;

  public TokenEntity updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public static TokenEntity fromDomain(Token token){
    return TokenEntity.builder()
        .id(token.id())
        .refreshToken(token.refreshToken())
        .build();
  }

  public static TokenEntity of(String id, String refreshToken) {
    return TokenEntity.builder()
        .id(id)
        .refreshToken(refreshToken)
        .build();
  }

  public Token toToken() {
    return Token.builder()
      .id(id)
      .refreshToken(refreshToken)
      .build();
  }

}
