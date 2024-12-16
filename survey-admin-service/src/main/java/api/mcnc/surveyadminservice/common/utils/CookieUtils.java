package api.mcnc.surveyadminservice.common.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-15 오후 11:53
 */
public class CookieUtils {

  private static final String COOKIE_NAME = "refreshToken";


  public static String setRefreshTokenCookie(String refreshToken) {

    ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, refreshToken)
//      .httpOnly(true)
//      .secure(false)
      .maxAge(60 * 60 * 24 * 14)
      .path("/")
      .sameSite("Lax")
      .build();
    return cookie.toString();
  }
}
