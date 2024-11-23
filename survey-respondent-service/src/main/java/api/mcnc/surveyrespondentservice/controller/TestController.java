package api.mcnc.surveyrespondentservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-21 오전 10:29
 */
@Controller
public class TestController {

  @GetMapping("/respondent/index")
  public String test() {
    return "index";
  }

}
