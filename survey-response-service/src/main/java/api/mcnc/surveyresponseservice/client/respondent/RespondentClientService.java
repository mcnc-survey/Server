package api.mcnc.surveyresponseservice.client.respondent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * open feign 응답자 유효성 체크 서비스
 *
 * @author :유희준
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
