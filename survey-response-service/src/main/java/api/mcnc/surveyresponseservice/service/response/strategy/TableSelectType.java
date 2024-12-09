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
 * @since :2024-11-17 오후 8:00
 */
public class TableSelectType implements QuestionTypeIfs {
  @Override
  public List<Object> calculateResponseResult(List<Response> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }

    Map<String, Integer> resultMap = new HashMap<>(DEFAULT_COLUMN_SIZE_OPACITY);// column의 최대 개수 - 10개 이상 갈 일은 잘 없을 것으로 생각 됨
    StringBuilder numBuffer = new StringBuilder(DEFAULT_COLUMN_LEN_OPACITY);// column의 최대 개수의 자리수 - 9개 이상 갈 일도 없을것 같지만 혹시 몰라 2자리로

    for (Response res : values) {
      String response = res.response();
      int length = response.length();

      for (int i = 0; i < length; i++) {
        if (response.charAt(i) == ':') {
          numBuffer.setLength(0);
          i++;
          while (i < length && response.charAt(i) != ',') {
            numBuffer.append(response.charAt(i++));
          }
          String num = numBuffer.toString();
          resultMap.merge(num, 1, Integer::sum);
        }
      }
    }

//    return resultMap;
    // TODO 2024-12-09 yhj : TABLE_SELECT는 당장에는 사용하지 않음
    return null;
  }
}
