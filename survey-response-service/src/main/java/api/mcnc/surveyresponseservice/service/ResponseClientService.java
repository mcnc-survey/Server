package api.mcnc.surveyresponseservice.service;

import api.mcnc.surveyresponseservice.repository.response.ResponseRepository;
import api.mcnc.surveyresponseservice.service.request.UpdateTypeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2025-01-03 오전 11:28
 */
@Service
@RequiredArgsConstructor
public class ResponseClientService {
  private final ResponseRepository responseRepository;

  public void updateType(String surveyId, List<UpdateTypeCommand> commandList) {
    responseRepository.updateType(surveyId, commandList);
  }

}
