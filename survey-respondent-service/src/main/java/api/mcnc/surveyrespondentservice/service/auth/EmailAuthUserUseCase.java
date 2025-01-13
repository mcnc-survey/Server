package api.mcnc.surveyrespondentservice.service.auth;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;

/**
 * 이메일 유저 usecase
 *
 * @author :유희준
 * @since :2024-11-22 오후 4:48
 */
public interface EmailAuthUserUseCase {
  AuthenticatedRespondent authenticateEmailUser(String provider, String code);
}
