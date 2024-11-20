package api.mcnc.surveyservice.controller;

import api.mcnc.surveyservice.common.enums.SuccessCode;
import api.mcnc.surveyservice.common.result.Api;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.service.survey.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오전 9:50
 */
@RestController
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyService surveyService;

  @PostMapping("/surveys")
  public Api<Void> createSurvey(@RequestBody @Valid SurveyCreateRequest surveyCreateRequest) {
    surveyService.setSurveyAndQuestions(surveyCreateRequest);
    return Api.ok(SuccessCode.SURVEY_CREATE_SUCCESS, null);
  }

}
