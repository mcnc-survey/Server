package api.mcnc.surveyrespondentservice.service.token;

import api.mcnc.surveyrespondentservice.authentication.jwt.JwtProvider;
import api.mcnc.surveyrespondentservice.authentication.jwt.TokenExtractResponse;
import api.mcnc.surveyrespondentservice.domain.Respondent;
import api.mcnc.surveyrespondentservice.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 토큰 서비스
 *
 * @author :유희준
 * @since :2024-11-23 오후 3:16
 */
@Service
@RequiredArgsConstructor
public class TokenService {
  private final JwtProvider tokenProvider;

  /**
   *  토큰 생성
   * @param respondent  응답자
   * @return             토큰
   */
  public Token generateTokenByRespondent(Respondent respondent) {
    String subject = respondent.id();
    Map<String, Object> claims = new HashMap<>();
    claims.put("name", respondent.name());
    claims.put("email", respondent.email());
    claims.put("surveyId", respondent.surveyId());

    String accessToken = tokenProvider.createToken(claims, subject);
    return Token.of(accessToken);
  }

  public TokenExtractResponse extractSubject(String token) {
    return tokenProvider.extractSubject(token);
  }
}
