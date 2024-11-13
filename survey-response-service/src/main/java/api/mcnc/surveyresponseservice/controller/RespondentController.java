package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.domain.respondent.RespondentCreateRequest;
import api.mcnc.surveyresponseservice.entity.respondent.RespondentEntity;
import api.mcnc.surveyresponseservice.repository.RespondentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequiredArgsConstructor
public class RespondentController {

  private final RespondentRepository respondentRepository;

  @RequestMapping("")
  public String respondent() {
    return "respondent";
  }

  @PostMapping
  public RespondentEntity createRespondent(@RequestBody RespondentCreateRequest request) {
    return respondentRepository.save(RespondentEntity.from(request));
  }
}
