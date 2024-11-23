package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.common.enums.RespondentErrorCode;
import api.mcnc.surveyrespondentservice.common.exception.custom.RespondentException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 10:12
 */
@RestController
@RequiredArgsConstructor
public class RespondentController {
  @GetMapping("/respondent")
  public String getRespondent() {
    throw new RespondentException(RespondentErrorCode.INVALID_REQUEST);
//    return "respondent";
  }
}
