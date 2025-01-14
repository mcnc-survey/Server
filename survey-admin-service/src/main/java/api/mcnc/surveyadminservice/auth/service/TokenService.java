package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.common.exception.TokenException;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.redis.TokenEntity;
import api.mcnc.surveyadminservice.redis.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static api.mcnc.surveyadminservice.common.enums.TokenErrorCode.TOKEN_EXPIRED;

/**
 * Token 관련 서비스
 * @author 유희준
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  /**
   * 토큰 삭제
   * @param adminId 관리자 아이디
   */
  public void deleteRefreshToken(String adminId) {
    tokenRepository.deleteById(adminId);
  }

  /**
   * 토큰 저장
   * @param adminId 관리자 아이디
   * @param refreshToken 리프레시 토큰
   * @param accessToken 액세스 토큰
   * @return {@link Token}
   */
  public Token saveOrUpdate(String adminId, String refreshToken, String accessToken) {
    TokenEntity token = tokenRepository.findByAccessToken(accessToken)
      .map(o -> o.updateRefreshToken(refreshToken))
      .orElseGet(() -> TokenEntity.of(adminId, refreshToken, accessToken));
    TokenEntity save = tokenRepository.save(token);
    return save.toToken();
  }

  /**
   * 액세스 토큰으로 토큰 조회
   * @param accessToken 액세스 토큰
   * @return {@link Token}
   */
  @Transactional(readOnly = true)
  public Token findByAccessTokenOrThrow(String accessToken) {
    return tokenRepository.findByAccessToken(accessToken)
      .map(TokenEntity::toToken)
      .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
  }

  /**
   * 토큰 재발급
   * @param accessToken  액세스 토큰
   * @param refreshToken 리프레시 토큰
   * @param token        토큰
   * @return {@link Token}
   */
  public Token updateToken(String accessToken, String refreshToken, Token token) {
    TokenEntity tokenEntity = TokenEntity.fromDomain(token);
    tokenEntity.updateAccessToken(accessToken);
    tokenEntity.updateRefreshToken(refreshToken);
    return tokenRepository.save(tokenEntity).toToken();
  }

}
