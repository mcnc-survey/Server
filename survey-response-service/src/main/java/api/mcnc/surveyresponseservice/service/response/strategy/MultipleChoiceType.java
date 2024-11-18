package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.domain.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 8:00
 */
public class MultipleChoiceType implements QuestionTypeIfs {
  @Override
  public Object calculateResponseResult(List<Response> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }
    return values.stream()
      .map(res -> res.response().split(","))
      .flatMap(Arrays::stream)              // 배열을 하나의 스트림으로 평탄화
      .collect(Collectors.groupingBy(      // 숫자별로 그룹화
        num -> num,                      // 그룹 기준 (문자열 그대로 사용)
        Collectors.counting()            // 각 그룹의 개수를 셈
      ));
  }
}
