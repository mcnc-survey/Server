package api.mcnc.surveyrespondentservice.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 10:25
 */
@Component
public class JwtProvider {

  private final SecretKey secretKey;

  @Value("${jwt.secret-key}")
  private String jwtSecret;

  public JwtProvider() {
    this.secretKey =  Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
      .claims(claims)
      .subject(subject)
      .issuedAt(new Date())
      .signWith(secretKey)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

}
