package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.authentication.jwt.TokenExtractResponse;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 9:41
 */
public interface ValidateUseCase {
  boolean validateRespondent(String respondentId);
  TokenExtractResponse extractSubject(String token);
}
