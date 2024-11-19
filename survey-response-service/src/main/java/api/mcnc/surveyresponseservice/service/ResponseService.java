package api.mcnc.surveyresponseservice.service;

import api.mcnc.surveyresponseservice.common.enums.ResponseErrorCode;
import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.controller.response.ResponseResult;
import api.mcnc.surveyresponseservice.domain.Response;
import api.mcnc.surveyresponseservice.repository.response.ResponseRepository;
import api.mcnc.surveyresponseservice.service.request.UpdateCommand;
import api.mcnc.surveyresponseservice.service.validation.ResponseValidator;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-14 오후 11:33
 */
@Service
@RequiredArgsConstructor
public class ResponseService {

  private final ResponseRepository responseRepository;
  private final ResponseValidator validator;

  public List<ResponseResult> getAllMyResponseResults(String surveyId, String respondentId) {
    return responseRepository.getRespondentResponseList(surveyId, respondentId)
      .stream()
      .map(Response::toResponseResult)
      .toList();
  }

  public void setResponse(String surveyId, String respondentId, List<QuestionResponse> request) {
    if (request == null || request.isEmpty()) {
      throw new ResponseException(ResponseErrorCode.EMPTY_RESPONSE);
    }
    // 검증
    validator.validateResponses(request);

    List<Response> responseList = request.stream()
      .map(res -> Response.of(surveyId, respondentId, res))
      .toList();
    responseRepository.responseSurvey(surveyId, respondentId, responseList);
  }

  public void updateResponse(String surveyId, String respondentId, List<QuestionResponseUpdate> request) {
    if (request == null || request.isEmpty()) {
      throw new ResponseException(ResponseErrorCode.EMPTY_RESPONSE);
    }
    // 검증
    validator.validateResponseUpdates(request);

    Map<String, UpdateCommand> updateMap = request.stream()
      .map(UpdateCommand::of)
      // id를 키로하는 map을 생성해서 전달
      .collect(Collectors.toMap(UpdateCommand::id, Function.identity()));
    responseRepository.updateResponse(surveyId, respondentId, updateMap);
  }

}