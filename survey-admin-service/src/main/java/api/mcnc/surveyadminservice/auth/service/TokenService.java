package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.common.exception.TokenException;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.entity.TokenEntity;
import api.mcnc.surveyadminservice.repository.token.TokenRepository;
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

  public void saveOrUpdate(String adminId, String refreshToken) {
    TokenEntity token = tokenRepository.findByRefreshToken(refreshToken)
      .map(o -> o.updateRefreshToken(refreshToken))
      .orElseGet(() -> TokenEntity.of(adminId, refreshToken));

    tokenRepository.save(token);
  }

  @Transactional(readOnly = true)
  public Token findByRefreshTokenOrThrow(String accessToken) {
    return tokenRepository.findByRefreshToken(accessToken)
      .map(TokenEntity::toToken)
      .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
  }

  public void updateToken(String accessToken, Token token) {
    TokenEntity tokenEntity = TokenEntity.fromDomain(token);
    tokenEntity.updateRefreshToken(accessToken);
    tokenRepository.save(tokenEntity);
  }

}
