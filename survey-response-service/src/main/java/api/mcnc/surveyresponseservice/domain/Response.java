package api.mcnc.surveyresponseservice.domain;

import api.mcnc.surveyresponseservice.common.constants.Constants;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.controller.response.ResponseResult;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import api.mcnc.surveyresponseservice.entity.response.ResponseEntity;
import lombok.Builder;
import org.yaml.snakeyaml.util.EnumUtils;

import static api.mcnc.surveyresponseservice.common.constants.Constants.SEPARATOR;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 11:22
 */
@Builder
public record Response(
  String id,
  String surveyId,
  String respondentId,
  String questionId,
  QuestionType questionType,
  Integer orderNumber,
  String response
) {

  public static Response of(String surveyId, String respondentId, QuestionResponse saveRequest) {
    return Response.builder()
      .surveyId(surveyId)
      .respondentId(respondentId)
      .questionId(saveRequest.questionId())
      .questionType(saveRequest.questionType())
      .orderNumber(saveRequest.orderNumber())
      .response(saveRequest.response())
      .build();
  }

  public static Response of(QuestionResponseUpdate responseUpdateRecord) {
    return Response.builder()
      .id(responseUpdateRecord.id())
      .response(responseUpdateRecord.response())
      .build();
  }

  public ResponseEntity toEntity() {
    return ResponseEntity.of(this);
  }

  public ResponseResult toResponseResult() {
    Object response = this.response;
    // 다중 선택 이면 선택 항목 배열로 반환
    if (QuestionType.MULTIPLE_CHOICE.equals(this.questionType)) {
      response = this.response.split(SEPARATOR);
    }
    return ResponseResult.builder()
      .id(this.id)
      .response(response)
      .build();
  }
}
