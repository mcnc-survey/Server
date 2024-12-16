package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.common.exception.TokenException;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.redis.TokenEntity;
import api.mcnc.surveyadminservice.redis.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static api.mcnc.surveyadminservice.common.enums.TokenErrorCode.TOKEN_EXPIRED;


@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public void deleteRefreshToken(String adminId) {
    tokenRepository.deleteById(adminId);
  }

  public Token saveOrUpdate(String adminId, String refreshToken, String accessToken) {
    TokenEntity token = tokenRepository.findByAccessToken(accessToken)
      .map(o -> o.updateRefreshToken(refreshToken))
      .orElseGet(() -> TokenEntity.of(adminId, refreshToken, accessToken));
    TokenEntity save = tokenRepository.save(token);
    return save.toToken();
  }

  @Transactional(readOnly = true)
  public Token findByAccessTokenOrThrow(String accessToken) {
    return tokenRepository.findByAccessToken(accessToken)
      .map(TokenEntity::toToken)
      .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
  }

  public Token updateToken(String accessToken, String refreshToken, Token token) {
    TokenEntity tokenEntity = TokenEntity.fromDomain(token);
    tokenEntity.updateAccessToken(accessToken);
    tokenEntity.updateRefreshToken(refreshToken);
    return tokenRepository.save(tokenEntity).toToken();
  }

}
