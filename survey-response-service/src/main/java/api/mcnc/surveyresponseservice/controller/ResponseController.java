package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.common.enums.SuccessCode;
import api.mcnc.surveyresponseservice.common.result.Api;
import api.mcnc.surveyresponseservice.controller.request.ResponseSaveRequest;
import api.mcnc.surveyresponseservice.controller.request.ResponseUpdateRequest;
import api.mcnc.surveyresponseservice.controller.response.ResponseResult;
import api.mcnc.surveyresponseservice.controller.response.SurveyResponsesResponse;
import api.mcnc.surveyresponseservice.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 응답 컨트롤러
 *
 * @author :유희준
 * @since :2024-11-15 오전 9:22
 */
@RestController
@RequiredArgsConstructor
public class ResponseController {

  private final ResponseService responseService;

  @GetMapping("/responses/{surveyId}")
  public Api<SurveyResponsesResponse> getAllResponse(@PathVariable("surveyId") String surveyId) {
    SurveyResponsesResponse results = responseService.getAllMyResponseResults(surveyId);
    return Api.ok(SuccessCode.SUCCESS, results);
  }

  @PostMapping("/responses/{surveyId}")
  public Api<Void> response(@PathVariable("surveyId") String surveyId, @RequestBody @Valid ResponseSaveRequest saveRequest) {
    responseService.setResponse(surveyId, saveRequest.responses());
    return Api.ok(SuccessCode.RESPONSE_CREATE_SUCCESS, null);
  }

  @PutMapping("/responses/{surveyId}")
  public Api<Void> responseUpdate(@PathVariable("surveyId") String surveyId, @RequestBody @Valid ResponseUpdateRequest saveRequest) {
    responseService.updateResponse(surveyId, saveRequest.responses());
    return Api.ok(SuccessCode.RESPONSE_UPDATE_SUCCESS, null);
  }
}
