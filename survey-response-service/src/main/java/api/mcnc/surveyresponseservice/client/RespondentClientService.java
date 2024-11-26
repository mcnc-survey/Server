package api.mcnc.surveyresponseservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-23 오후 8:22
 */
@Service
@RequiredArgsConstructor
public class RespondentClientService implements RespondentValidate{
  private final RespondentClient respondentClient;

  @Override
  public boolean isExistRespondent(String respondentId) {
    return respondentClient.validateRespondent(respondentId);
  }
}
