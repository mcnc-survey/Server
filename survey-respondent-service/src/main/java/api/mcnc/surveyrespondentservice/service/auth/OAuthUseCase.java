package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedUser;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 3:12
 */
public interface OAuthUseCase {
  AuthenticatedUser getAuthenticatedSocialUser(String provider, String code);
}
