package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedUser;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 4:48
 */
public interface EmailAuthUserUseCase {
  AuthenticatedUser authenticateEmailUser(String provider, String code);
}
