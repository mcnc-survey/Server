package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import api.mcnc.surveyrespondentservice.domain.Token;

/**
 * 등록 usecase
 *
 * @author :유희준
 * @since :2024-11-23 오후 3:37
 */
public interface RegisterUseCase {
  Token registerRespondent(AuthenticatedRespondent authenticatedUser, String surveyId);
}
