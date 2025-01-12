package api.mcnc.surveyadminservice.common.utils;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

/**
 * 쿠키 설정 클래스
 *
 * @author 유희준
 * @since :2024-12-15 오후 11:53
 */
public class CookieUtils {

  private static final String COOKIE_NAME = "refreshToken";
  private static final String COOKIE_HEADER_NAME = "Set-Cookie";

  /**
   * 쿠키에서 refreshToken 추출
   * @param request {@link HttpServletRequest}
   * @return refreshToken
   */
  public static String extractRefreshTokenInCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if(cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("refreshToken")) {
          return cookie.getValue();
        }
      }
    }
    throw new AdminException(AdminErrorCode.INVALID_REQUEST, "refreshToken 값이 null일 수 없습니다.");
  }

  public static void setCookie(String refreshToken, HttpServletResponse response) {
    String cookie = generateRefreshTokenCookie(refreshToken);
    response.setHeader(COOKIE_HEADER_NAME, cookie);
  }

  public static String generateRefreshTokenCookie(String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, refreshToken)
      .httpOnly(true)
      .secure(true)
      .maxAge(60 * 60 * 24 * 30)
      .path("/")
      .sameSite("None")
      .build();
    return cookie.toString();
  }

  public static void deleteCookie(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME)
//      .httpOnly(true)
//      .secure(false)
      .maxAge(0)
      .path("/")
      .sameSite("Lax")
      .build();
    response.setHeader(COOKIE_HEADER_NAME, cookie.toString());
  }
}
