package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.controller.response.aggregation.QuestionSnippet;
import api.mcnc.surveyresponseservice.domain.Response;

import java.util.*;

import static api.mcnc.surveyresponseservice.common.constants.Constants.SEPARATOR;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 8:00
 */
public class MultipleChoiceType implements QuestionTypeIfs {
  @Override
  public List<Object> calculateResponseResult(List<Response> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }
// 결과를 저장할 맵 초기화
    Map<String, Integer> resultMap = new HashMap<>(DEFAULT_COLUMN_SIZE_OPACITY);

    // 각 응답을 순회하며 결과 맵에 카운트
    for (Response response : values) {
      // 응답 문자열이 null이 아닌 경우에만 처리
      if (response.response() != null) {
        // |`| 구분자로 문자열 분리
        String[] responses = response.response().split(SEPARATOR);

        // 분리된 각 응답에 대해 카운트 증가
        for (String res : responses) {
          resultMap.merge(res, 1, Integer::sum);
        }
      }
    }

    // 결과를 저장할 QuestionSnippet 리스트 생성
    List<Object> responses = new ArrayList<>();

    // 결과 맵의 각 키에 대해 QuestionSnippet 생성
    for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
      responses.add(QuestionSnippet.of(entry.getKey(), entry.getValue()));
    }
    // QuestionSnippet 리스트 반환
    return responses;
  }
}
