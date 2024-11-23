package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.client.oauth.SocialService;
import api.mcnc.surveyrespondentservice.client.oauth.UserInfo;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오전 9:16
 */
@Service
@RequiredArgsConstructor
public class AuthService implements OAuthUseCase, EmailAuthUserUseCase {
  private final SocialService socialService;

  @Override
  public AuthenticatedUser getAuthenticatedSocialUser(String provider, String code) {
    UserInfo socialUserInfo = socialService.getAuthenticatedSocialUserInfo(provider, code);
    return AuthenticatedUser.of(socialUserInfo, provider);
  }

  @Override
  public AuthenticatedUser authenticateEmailUser(String provider, String code) {
    return null;
  }

}
