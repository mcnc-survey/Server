package api.mcnc.surveyresponseservice.service.response.strategy;

import api.mcnc.surveyresponseservice.domain.Response;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-17 오후 6:43
 */
public interface QuestionTypeIfs {
  Integer DEFAULT_COLUMN_SIZE_OPACITY = 10;
  Integer DEFAULT_COLUMN_LEN_OPACITY = 2;

  List<Object> calculateResponseResult(List<Response> values);

}
