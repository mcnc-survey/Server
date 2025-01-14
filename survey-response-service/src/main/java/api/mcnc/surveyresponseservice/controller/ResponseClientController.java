package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.controller.request.ResponseUpdate;
import api.mcnc.surveyresponseservice.service.DeleteScheduleService;
import api.mcnc.surveyresponseservice.service.ResponseClientService;
import api.mcnc.surveyresponseservice.service.request.UpdateSurveyCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 응답의 삭제 수정 컨트롤러
 *
 * @author :유희준
 * @since :2024-12-19 오후 5:01
 */
@RestController
@RequiredArgsConstructor
public class ResponseClientController {

  private final DeleteScheduleService deleteService;
  private final ResponseClientService responseService;

  @DeleteMapping("/deletion/responses")
  public void deleteResponse(@RequestBody List<String> surveyIdList) {
    deleteService.deleteResponse(surveyIdList);
  }

  @PutMapping("/update/response")
  void updateResponse(@RequestBody List<ResponseUpdate> responseUpdateList) {
    String surveyId = responseUpdateList.get(0).surveyId();
    List<UpdateSurveyCommand> updateCommandList = responseUpdateList.stream()
      .map(UpdateSurveyCommand::of)
      .toList();
    responseService.updateType(surveyId, updateCommandList);
  }

}
