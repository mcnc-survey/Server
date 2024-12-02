package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.domain.AuthenticatedUser;
import api.mcnc.surveyrespondentservice.domain.Token;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 3:37
 */
public interface RegisterUseCase {
  Token registerRespondent(AuthenticatedUser authenticatedUser, String surveyId);
}
