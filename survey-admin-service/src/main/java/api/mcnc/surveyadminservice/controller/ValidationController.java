package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.controller.request.TokenValidateRequest;
import api.mcnc.surveyadminservice.controller.response.TokenValidateResponse;
import api.mcnc.surveyadminservice.service.AdminValidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-09 오후 12:34
 */
@RestController
@RequiredArgsConstructor
public class ValidationController {

  private final TokenProvider tokenProvider;
  private final AdminValidService adminValidService;

  @PostMapping("/token-validation")
  public TokenValidateResponse validateToken(@RequestBody @Valid TokenValidateRequest request) {
    return tokenProvider.validateToken(request.accessToken());
  }

  @GetMapping("/admin-validation/{adminId}")
  boolean adminValidation(@PathVariable String adminId) {
    return adminValidService.isExistById(adminId);
  }
}
