package api.mcnc.surveyresponseservice.client.survey.response;

import api.mcnc.surveyresponseservice.common.constants.Constants;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

import static api.mcnc.surveyresponseservice.entity.response.QuestionType.SINGLE_CHOICE;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:01
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Question(
  String id,
  String title,
  QuestionType questionType,
  Integer order,
  String columns,
  String rows,
  Boolean required,
  Boolean etc
) {
  public Object getColumns() {
    if (QuestionType.SINGLE_CHOICE.equals(this.questionType) || QuestionType.MULTIPLE_CHOICE.equals(this.questionType)) {
      return List.of(columns.split(Constants.SEPARATOR));
    }
    return columns;
  }
}
