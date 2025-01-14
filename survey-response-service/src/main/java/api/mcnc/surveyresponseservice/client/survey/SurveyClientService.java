package api.mcnc.surveyresponseservice.client.survey;

import api.mcnc.surveyresponseservice.client.survey.response.Survey;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode.NOT_FOUND_SURVEY;

/**
 * 유효성 서비스
 *
 * @author :유희준
 * @since :2024-11-26 오후 11:24
 */
@Service
@RequiredArgsConstructor
public class SurveyClientService implements SurveyValidate, SurveyResponse{

  private final SurveyClient surveyClient;

  @Override
  public boolean isExistSurvey(String surveyId) {
    return surveyClient.validateSurvey(surveyId);
  }

  @Override
  public Survey getSurvey(String surveyId) {
    return Optional.ofNullable(surveyClient.getSurvey(surveyId))
      .orElseThrow(() -> new ResponseException(NOT_FOUND_SURVEY));
  }
}
