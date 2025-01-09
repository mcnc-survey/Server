package api.mcnc.surveyresponseservice.client.survey.response;

import api.mcnc.surveyresponseservice.common.constants.Constants;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-27 오후 2:01
 */
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question{
  private String id;
  private String title;
  private QuestionType questionType;
  private Integer order;
  private String columns;
  private String rows;
  private Boolean required;
  private Boolean etc;

  public Object getColumns() {
    if (QuestionType.SINGLE_CHOICE.equals(this.questionType) || QuestionType.MULTIPLE_CHOICE.equals(this.questionType)) {
      return List.of(columns.split(Constants.SEPARATOR));
    }
    return columns;
  }
}
