package api.mcnc.surveyrespondentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-12 오전 10:49
 */
@RestController
@RequestMapping("/respondent")
public class RespondentController {

  @RequestMapping("")
  public String respondent() {
    return "respondent";
  }

}
