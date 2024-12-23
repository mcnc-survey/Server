package api.mcnc.surveyresponseservice.service;

import api.mcnc.surveyresponseservice.repository.delete.DeleteQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오후 7:55
 */
@Service
@RequiredArgsConstructor
public class DeleteScheduleService {


  private final DeleteQueueRepository deleteQueueRepository;

  public void deleteResponse(List<String> surveyId) {
    deleteQueueRepository.waitDeletion(surveyId);
  }


}
