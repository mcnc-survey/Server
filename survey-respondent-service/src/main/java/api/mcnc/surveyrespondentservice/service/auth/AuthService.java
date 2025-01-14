package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.client.oauth.SocialService;
import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스
 *
 * @author :유희준
 * @since :2024-11-22 오전 9:16
 */
@Service
@RequiredArgsConstructor
public class AuthService implements OAuthUseCase, EmailAuthUserUseCase {
  private final SocialService socialService;

  /**
   * 인증된 사용자 정보를 가져온다.
   * @param provider  소셜 로그인 제공자
   * @param code      소셜 로그인 코드
   * @return          인증된 사용자 정보
   */
  @Override
  public AuthenticatedRespondent getAuthenticatedSocialUser(String provider, String code) {
    UserInfo socialUserInfo = socialService.getAuthenticatedSocialUserInfo(provider, code);
    return AuthenticatedRespondent.of(socialUserInfo, provider);
  }

  /**
   * 이메일 사용자 인증
   * @param provider  소셜 로그인 제공자
   * @param code      소셜 로그인 코드
   * @return          인증된 사용자 정보
   */
  @Override
  public AuthenticatedRespondent authenticateEmailUser(String provider, String code) {
    return null;
  }

}
