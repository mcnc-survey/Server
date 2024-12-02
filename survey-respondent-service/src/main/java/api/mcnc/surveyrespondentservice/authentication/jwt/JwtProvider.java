package api.mcnc.surveyrespondentservice.authentication.jwt;

import api.mcnc.surveyrespondentservice.common.exception.custom.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

import static api.mcnc.surveyrespondentservice.common.enums.TokenErrorCode.INVALID_JWT_SIGNATURE;
import static api.mcnc.surveyrespondentservice.common.enums.TokenErrorCode.INVALID_TOKEN;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 10:25
 */
@Component
public class JwtProvider {

  @Value("${jwt.secret-key}")
  private String key;
  private SecretKey secretKey;

  @PostConstruct
  private void setSecretKey() {
    secretKey = Keys.hmacShaKeyFor(key.getBytes());
  }

  public String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();

    return Jwts.builder()
      .subject(subject)
      .claims(claims)
      .issuedAt(now)
      .expiration(new Date(now.getTime() + 1000))
      .signWith(secretKey, Jwts.SIG.HS512)
      .compact();
  }

  public String extractSubject(String token) {
    Claims claims = parseClaims(token);
    return claims.getSubject();
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    } catch (MalformedJwtException e) {
      throw new TokenException(INVALID_TOKEN);
    } catch (SecurityException e) {
      throw new TokenException(INVALID_JWT_SIGNATURE);
    }
  }


}
