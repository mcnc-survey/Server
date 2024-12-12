package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.client.oauth.SocialService;
import api.mcnc.surveyrespondentservice.authentication.auth.UserInfo;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
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
  public AuthenticatedRespondent getAuthenticatedSocialUser(String provider, String code) {
    UserInfo socialUserInfo = socialService.getAuthenticatedSocialUserInfo(provider, code);
    return AuthenticatedRespondent.of(socialUserInfo, provider);
  }

  @Override
  public AuthenticatedRespondent authenticateEmailUser(String provider, String code) {
    return null;
  }

}
