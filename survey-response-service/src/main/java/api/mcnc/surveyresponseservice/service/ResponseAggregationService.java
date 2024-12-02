package api.mcnc.surveyresponseservice.service;

import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.repository.response.ResponseAggregationRepository;
import api.mcnc.surveyresponseservice.service.response.ResponseAggregationResponse;
import api.mcnc.surveyresponseservice.service.response.ResponseResultByQuestionType;
import api.mcnc.surveyresponseservice.service.validation.ValidOtherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 9:08
 */
@Service
@RequiredArgsConstructor
public class ResponseAggregationService {

  private final ResponseResultByQuestionType responseResult;
  private final ResponseAggregationRepository aggregationRepository;
  private final ValidOtherService validService;

  public ResponseAggregationResponse getResponseAggregationBySurveyId(String surveyId) {
    validService.validateSurvey(surveyId);

    Map<Integer, List<Response>> responseList = aggregationRepository.getResponseListMappingByOrderNumberBySurveyId(surveyId);

    Map<Integer, Object> result = new HashMap<>();
    for(Map.Entry<Integer, List<Response>> responseListEntry :responseList.entrySet()){
      List<Response> values = responseListEntry.getValue();

      Integer key = responseListEntry.getKey();
      Object value = responseResult.calculateResponseResult(values);

      result.put(key, value);
    }

    Integer totalRespondentCount = aggregationRepository.getRespondentCountBySurveyId(surveyId);
    return new ResponseAggregationResponse(totalRespondentCount, result);
  }


}
