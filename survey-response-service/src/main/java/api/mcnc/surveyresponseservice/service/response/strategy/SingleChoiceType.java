package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.domain.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 7:53
 */
public class SingleChoiceType implements QuestionTypeIfs {
  @Override
  public Object calculateResponseResult(List<Response> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }
    Map<String, Integer> resultMap = new HashMap<>(DEFAULT_COLUMN_SIZE_OPACITY);

    for (Response response: values) {
      String result = response.response();
      if (result != null) {
        resultMap.merge(result, 1, Integer::sum);
      }
    }
    return resultMap;
  }
}
