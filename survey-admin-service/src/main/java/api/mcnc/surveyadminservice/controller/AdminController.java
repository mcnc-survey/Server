package api.mcnc.surveyadminservice.controller;

import org.springframework.web.bind.annotation.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-12 오전 10:46
 */
@RestController
public class AdminController {

  @GetMapping("admin")
  public String admin() {
    return "admin";
  }

  @GetMapping("/{oauth}/redirection")
  public String redirection(@PathVariable String oauth, @RequestParam(required = false, value = "state") String state) {
    System.out.println(oauth);
    System.out.println(state);
    return "redirection";
  }


}
