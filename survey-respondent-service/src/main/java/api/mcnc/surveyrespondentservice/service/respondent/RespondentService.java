package api.mcnc.surveyrespondentservice.service.respondent;

import api.mcnc.surveyrespondentservice.authentication.jwt.TokenExtractResponse;
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
 * 등록 서비스 구현체
 * 응답자 등록, 응답자 유효성 검사, 토큰 발급 서비스
 *
 * @author :유희준
 * @since :2024-11-22 오후 4:58
 */
@Service
@RequiredArgsConstructor
public class RespondentService implements RegisterUseCase,  ValidateUseCase{
  private final RespondentRepository respondentRepository;
  private final SurveyValidationService surveyValidationService;
  private final TokenService tokenService;

  /**
   * 등록
   * @param authenticatedUser  인증된 사용자 정보
   * @param surveyId          설문 아이디
   * @return                  토큰
   */
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

  /**
   * 응답자 유효성 검사
   * @param respondentId  응답자 아이디
   * @return              응답자 유효성 검사 결과
   */
  @Override
  public boolean validateRespondent(String respondentId) {
    return respondentRepository.isExist(respondentId);
  }

  /**
   *  토큰에서 응답자 아이디, 설문 아이디 추출
   * @param token  토큰
   * @return       응답자 아이디, 설문 아이디
   */
  @Override
  public TokenExtractResponse extractSubject(String token) {
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
