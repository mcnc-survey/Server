package api.mcnc.surveyservice.controller;

import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.service.survey.SurveyValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 5:43
 */
@RestController
@RequiredArgsConstructor
public class SurveyValidationController {

  private final SurveyValidationService surveyValidationService;

  // surveyId가 졵재하는 지 판단하는 api
  @GetMapping("/survey-validate/{surveyId}")
  public Boolean validateSurvey(@PathVariable("surveyId") String surveyId) {
    return surveyValidationService.existsBySurveyId(surveyId);
  }

  // 타 서비스에서 Survey 조회
  @GetMapping("/survey-get/{surveyId}")
  public Survey getValidSurvey(@PathVariable("surveyId") String surveyId) {
    return surveyValidationService.getSruvey(surveyId);
  }

}
