package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.authentication.jwt.TokenExtractResponse;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.service.respondent.ValidateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 9:39
 */
@RestController
@RequiredArgsConstructor
public class ValidationController {

  private final ValidateUseCase validateUseCase;

  @GetMapping("/respondent-validation/{respondentId}")
  boolean validateRespondent(@PathVariable String respondentId){
    return validateUseCase.validateRespondent(respondentId);
  }

  @PostMapping("/token-validation")
  TokenExtractResponse extractSubject(@RequestBody Token token){
    return validateUseCase.extractSubject(token.accessToken());
  }

}
