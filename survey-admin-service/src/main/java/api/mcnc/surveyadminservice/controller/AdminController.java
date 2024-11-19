package api.mcnc.surveyadminservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-12 오전 10:46
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

  @GetMapping("")
  public String admin() {
    return "admin";
  }


}
