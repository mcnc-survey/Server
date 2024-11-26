package api.mcnc.surveyservice.controller;

import api.mcnc.surveyservice.common.enums.SuccessCode;
import api.mcnc.surveyservice.common.result.Api;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.SurveyCalendarResponse;
import api.mcnc.surveyservice.controller.response.SurveyDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyLikeResponse;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.service.survey.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping("/surveys")
  public Api<List<SurveyResponse>> getSurveyList() {
    List<SurveyResponse> surveyList = surveyService.getSurveyList();
    return Api.ok(SuccessCode.SUCCESS, surveyList);
  }

  @GetMapping("/surveys/calendar")
  public Api<List<SurveyCalendarResponse>> getSurveyListForCalendar() {
    List<SurveyCalendarResponse> surveyList = surveyService.getSurveyListForCalendar();
    return Api.ok(SuccessCode.SUCCESS, surveyList);
  }

  @GetMapping("/surveys/delete")
  public Api<List<SurveyResponse>> getSurveyListForDelete() {
    List<SurveyResponse> surveyList = surveyService.getSurveyListForDelete();
    return Api.ok(SuccessCode.SUCCESS, surveyList);
  }

  @GetMapping("/surveys/like")
  public Api<List<SurveyLikeResponse>> getSurveyLikeList() {
    List<SurveyLikeResponse> surveyList = surveyService.getSurveyLikeList();
    return Api.ok(SuccessCode.SUCCESS, surveyList);
  }

  @GetMapping("/surveys/survey-id/{surveyId}/edit")
  public Api<SurveyDetailsResponse> getSurveyDetailForEdit(@PathVariable("surveyId") String surveyId) {
    SurveyDetailsResponse surveyList = surveyService.getDetail(surveyId);
    return Api.ok(SuccessCode.SUCCESS, surveyList);
  }

  @PutMapping("/surveys/survey-id/{surveyId}")
  public Api<Void> updateSurvey(@PathVariable("surveyId") String surveyId, @RequestBody @Valid SurveyUpdateRequest surveyUpdateRequest) {
    surveyService.updateSurvey(surveyId, surveyUpdateRequest);
    return Api.ok(SuccessCode.SURVEY_UPDATE_SUCCESS, null);
  }

  @DeleteMapping("/surveys/survey-id/{surveyId}")
  public Api<Void> deleteSurvey(@PathVariable("surveyId") String surveyId) {
    surveyService.deleteSurvey(surveyId);
    return Api.ok(SuccessCode.SURVEY_DELETE_SUCCESS, null);
  }

  @GetMapping("/surveys/survey-id/{surveyId}/like")
  public Api<SurveyDetailsResponse> onSurveyBookmark(@PathVariable("surveyId") String surveyId) {
    surveyService.like(surveyId);
    return Api.ok(SuccessCode.SUCCESS, null);
  }

}
