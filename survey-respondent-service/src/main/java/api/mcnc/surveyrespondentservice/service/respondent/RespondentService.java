package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.client.survey.SurveyValidationService;
import api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode;
import api.mcnc.surveyrespondentservice.common.exception.custom.RespondentException;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import api.mcnc.surveyrespondentservice.domain.Respondent;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.repository.RespondentRepository;
import api.mcnc.surveyrespondentservice.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-22 오후 4:58
 */
@Service
@RequiredArgsConstructor
public class RespondentService implements RegisterUseCase,  ValidateUseCase{
  private final RespondentRepository respondentRepository;
  private final SurveyValidationService surveyValidationService;
  private final TokenService tokenService;

  @Override
  public Token registerRespondent(AuthenticatedRespondent authenticatedUser, String surveyId) {
//   존재하는 설문인지
    this.validateSurvey(surveyId);
//   응답 한 적 있는지
    Optional<Respondent> respondent = respondentRepository.getRespondent(authenticatedUser.email(), surveyId);
//   응답 한 적 없으면 저장
    Respondent token = respondent.orElseGet(
      () -> {
        Respondent saveRespondent = Respondent.of(authenticatedUser, surveyId);
        return respondentRepository.register(saveRespondent);
      }
    );
//    토큰 만들어서 반환
    return getToken(token);
  }

  @Override
  public boolean validateRespondent(String respondentId) {
    return respondentRepository.isExist(respondentId);
  }

  @Override
  public String extractSubject(String token) {
    return tokenService.extractSubject(token);
  }

  //  토큰 만들어서 반환
  private Token getToken(Respondent respondent) {
    return tokenService.generateTokenByRespondent(respondent);
  }

  private void validateSurvey(String surveyId) {
    // 설문인지 확인
    boolean isValid = surveyValidationService.validateExistAndStatus(surveyId);
    if(Boolean.FALSE.equals(isValid)){
      throw new RespondentException(RespondentErrorCode.INVALID_REQUEST, "존재하지 않거나 마감된 설문입니다.");
    }
  }

  private boolean validateExist(String email, String surveyId) {
    return respondentRepository.isExist(email, surveyId);
  }

}
