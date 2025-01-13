package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.authentication.jwt.TokenExtractResponse;

/**
 * 검증 usecase
 *
 * @author :유희준
 * @since :2024-11-26 오후 9:41
 */
public interface ValidateUseCase {
  boolean validateRespondent(String respondentId);
  TokenExtractResponse extractSubject(String token);
}
