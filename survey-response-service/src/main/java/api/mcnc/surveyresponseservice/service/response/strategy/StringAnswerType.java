package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.domain.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 8:00
 */
public class StringAnswerType implements QuestionTypeIfs {
  @Override
  public List<Object> calculateResponseResult(List<Response> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }
    // 응답 개수 만큼 다양한 응답이 생김
    List<Object> results = new ArrayList<>(values.size());

    for (Response value : values) {
      String response = value.response();
      if (response != null) {
        results.add(response);
      }
    }

    return results;
  }
}
