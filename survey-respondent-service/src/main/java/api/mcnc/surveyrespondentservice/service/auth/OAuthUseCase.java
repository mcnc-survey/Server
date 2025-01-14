package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;

/**
 * 소셜 유저 usecase
 *
 * @author :유희준
 * @since :2024-11-22 오후 3:12
 */
public interface OAuthUseCase {
  AuthenticatedRespondent getAuthenticatedSocialUser(String provider, String code);
}
