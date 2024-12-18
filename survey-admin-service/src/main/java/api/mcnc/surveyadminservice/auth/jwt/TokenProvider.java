package api.mcnc.surveyadminservice.auth.jwt;

import api.mcnc.surveyadminservice.auth.service.TokenService;
import api.mcnc.surveyadminservice.common.enums.TokenErrorCode;
import api.mcnc.surveyadminservice.common.exception.TokenException;
import api.mcnc.surveyadminservice.controller.response.TokenValidateResponse;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.entity.admin.AdminRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;


@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 14;
    private static final String KEY_ROLE = "role";
    private final TokenService tokenService;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    /**
     * 토큰 생성
     * @param authentication {@link Admin }(관리자) 정보
     * @return {@link Token}
     */
    public Token issue(Admin authentication) {
        String accessToken = generateAccessToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
        return generateRefreshToken(authentication, accessToken);
    }

    /**
     * 토큰 재발급
     * @param accessToken 요청 토큰
     * @return {@link Token}
     */
    public Token reissueAccessToken(String accessToken, String refreshToken) {
        // 요청 토큰 값이 빈 값일 수 없음
        if (!StringUtils.hasText(accessToken)) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        // 토큰 유효성 검증을 위한 요청
        parseClaims(accessToken);
        Token token = tokenService.findByAccessTokenOrThrow(accessToken);
        String originRefreshToken = token.refreshToken();

        // 요청한 곳의 refreshToken과 db의 refreshToken이 일치 하지 않는 것은 출처를 알 수 없는 요청임
        if (!originRefreshToken.equals(refreshToken)) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        // 리프레시 토큰이 유효하지 않으면
        if (!validateToken(refreshToken).isValid()) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        Admin authentication = getAuthentication(refreshToken);

        // 토큰에 관리자 정보가 존재하지 않으면
        if (authentication == null) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }

        // 리프레시 토큰에서 추출한 관리자 정보로 토큰 재생성
        Token reissueToken = issue(authentication);
        String reissueAccessToken = reissueToken.accessToken();
        String reissueRefreshToken = reissueToken.refreshToken();

        // 재생성한 토큰으로 저장 후 리턴
        return tokenService.updateToken(reissueAccessToken, reissueRefreshToken, token);
    }

    /**
     * 토큰 검증
     * @param accessToken 요청 토큰
     * @return {@link TokenValidateResponse}
     */
    public TokenValidateResponse validateToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return TokenValidateResponse.invalidResponse();
        }

        // 인증된 유저에대한 토큰인지 확인
        tokenService.findByAccessTokenOrThrow(accessToken);

        Claims claims = parseClaims(accessToken);
        boolean isValid = claims.getExpiration().after(new Date());

        if(isValid){
            return TokenValidateResponse.validResponse(claims.getSubject());
        } else {
            return TokenValidateResponse.invalidResponse();
        }
    }

    /**
     * 토큰 만료
     * @param accessToken 요청 토큰
     */
    public void expireToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        tokenService.deleteRefreshToken(claims.getSubject());
    }

    /**
     * 토큰 생성
     * @param authentication {@link Admin }(관리자) 정보
     * @return 토큰 - String
     */
    public String generateAccessToken(Admin authentication, long expireTime) {
        return generateToken(authentication, expireTime);
    }

    private Token generateRefreshToken(Admin authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        return tokenService.saveOrUpdate(authentication.id(), refreshToken, accessToken);
    }

    private String generateToken(Admin authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        AdminRole authorities = authentication.role();

        return Jwts.builder()
                .subject(authentication.id())
                .claim(KEY_ROLE, authorities)
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    private Admin getAuthentication(String token) {
        Claims claims = parseClaims(token);

        return Admin.builder()
                .id(claims.getSubject())
                .role(AdminRole.valueOf(claims.get(KEY_ROLE, String.class)))
                .build();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException | SecurityException e) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
    }
}
