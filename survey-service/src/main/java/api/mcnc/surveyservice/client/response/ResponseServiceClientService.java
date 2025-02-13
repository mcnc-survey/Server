package api.mcnc.surveyservice.client.response;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-19 오후 8:29
 */
@Service
@RequiredArgsConstructor
public class ResponseServiceClientService {

  private final ResponseServiceClient responseServiceClient;

  @Async
  public void deleteResponse(List<String> surveyIdList) {
    responseServiceClient.deleteResponse(surveyIdList);
  }

  @Async
  public void updateResponse(List<ResponseUpdate> responseUpdateList) {
    responseServiceClient.updateResponse(responseUpdateList);
  }
}
