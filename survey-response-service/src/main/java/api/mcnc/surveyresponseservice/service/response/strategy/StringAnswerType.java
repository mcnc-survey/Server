package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.domain.Response;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static api.mcnc.surveyresponseservice.common.constants.Constants.SEPARATOR;

/**
 * please explain class!
 *
 * @author :유희준
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

    for (Response response : values) {
      String[] responses = response.response().split(SEPARATOR);
      for (String res : responses) {
        if (StringUtils.hasText(res)) {
          results.add(res);
        }
      }
    }

    return results;
  }
}
