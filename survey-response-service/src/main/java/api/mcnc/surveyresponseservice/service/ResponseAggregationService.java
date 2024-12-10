package api.mcnc.surveyresponseservice.service;

import api.mcnc.surveyresponseservice.client.survey.response.Question;
import api.mcnc.surveyresponseservice.client.survey.response.Survey;
import api.mcnc.surveyresponseservice.controller.response.aggregation.SurveyResultValue;
import api.mcnc.surveyresponseservice.controller.response.aggregation.SurveySummary;
import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.repository.response.ResponseAggregationRepository;
import api.mcnc.surveyresponseservice.controller.response.aggregation.ResponseAggregationResponse;
import api.mcnc.surveyresponseservice.service.response.ResponseResultByQuestionType;
import api.mcnc.surveyresponseservice.service.validation.ValidOtherService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
    
    // 설문 id로 설문 상세 정보 조회
    Survey surveyDetailsResponse = validService.validateAndGetSurvey(surveyId);

    // 총 응답자 수
    Integer totalRespondentCount = aggregationRepository.getRespondentCountBySurveyId(surveyId);

    // snippet
    SurveySummary surveySummary = SurveySummary.of(
      totalRespondentCount,
      surveyDetailsResponse.endAt().toString(),
      surveyDetailsResponse.lastModifiedDate()
    );

    if(totalRespondentCount == 0) {
      return new ResponseAggregationResponse(surveySummary, new HashMap<>());
    }

    // 설문의 항목 순서별 응답 데이터 Map으로 가져오기 
    Map<Integer, List<Response>> responseList = aggregationRepository.getResponseListMappingByOrderNumberBySurveyId(surveyId);

    // 설문 질문 정보들
    List<Question> questionList = surveyDetailsResponse.question();

    // 응답 집계
    Map<Integer, SurveyResultValue> result = new HashMap<>();
    
    for (Question questionDetailsResponse : questionList) {
      // 질문 별
      Integer key = questionDetailsResponse.order();
      // 응답 데이터들
      List<Response> responsesGroupByOrderNumber = responseList.get(key);
      // 응답 데이터 집계 알고리즘
      Object responses = responseResult.calculateResponseResult(responsesGroupByOrderNumber);
      // 항목별 응답 객체
      SurveyResultValue surveyResultValue = SurveyResultValue.builder()
        .questionId(questionDetailsResponse.id())
        .questionTitle(questionDetailsResponse.title())
        .questionType(questionDetailsResponse.questionType())
        .totalResponseCount(responsesGroupByOrderNumber.size())
        .responses(responses)
        .build();

      // 전체 결과, [항목 번호 : 데이터]
      result.put(key, surveyResultValue);

    }


    return new ResponseAggregationResponse(surveySummary, result);
  }


}
