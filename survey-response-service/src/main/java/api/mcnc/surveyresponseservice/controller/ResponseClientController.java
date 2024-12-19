package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.service.DeleteScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오후 5:01
 */
@RestController
@RequiredArgsConstructor
public class ResponseClientController {

  private final DeleteScheduleService deleteService;

  @DeleteMapping("/deletion/responses")
  public void deleteResponse(List<String> surveyIdList) {
    deleteService.deleteResponse(surveyIdList);
  }

}
