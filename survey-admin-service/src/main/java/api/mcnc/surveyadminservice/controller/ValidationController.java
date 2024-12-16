package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.controller.request.TokenValidateRequest;
import api.mcnc.surveyadminservice.controller.response.TokenValidateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-09 오후 12:34
 */
@RestController
@RequiredArgsConstructor
public class ValidationController {

  private final TokenProvider tokenProvider;

  @PostMapping("/token-validation")
  public TokenValidateResponse validateToken(@RequestBody @Valid TokenValidateRequest request) {
    return tokenProvider.validateToken(request.accessToken());
  }
}
