//package api.mcnc.surveyresponseservice.service.validation;
//
//import api.mcnc.surveyresponseservice.common.exception.custom.ResponseException;
//import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
//import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
//import api.mcnc.surveyresponseservice.entity.response.QuestionType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * please explain class!
// *
// * @author :유희준
// * @since :2024-11-18 오전 10:06
// */
//@DisplayName("ResponseValidator 테스트")
//class ResponseValidatorTest {
//  private ResponseValidator validator;
//
//  @BeforeEach
//  void setUp() {
//    validator = new ResponseValidator();
//  }
//
//  @Nested
//  @DisplayName("단일 선택(SINGLE_CHOICE) 테스트")
//  class SingleChoiceTest {
//    @Test
//    @DisplayName("정상적인 단일 선택 응답은 통과해야 한다")
//    void validateSingleChoice_ShouldAcceptValidFormat() {
//      // given
//      QuestionResponse response = new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "1");
//
//      // when & then
//      assertDoesNotThrow(() ->
//        validator.validateResponses(List.of(response))
//      );
//    }
//
//    @Test
//    @DisplayName("잘못된 단일 선택 응답은 예외가 발생해야 한다")
//    void validateSingleChoice_ShouldRejectInvalidFormat() {
//      assertAll(
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "0")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "-1")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "1,2")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SINGLE_CHOICE, 1, true, "abc")
//          ))
//        )
//      );
//    }
//  }
//
//  @Nested
//  @DisplayName("다중 선택(MULTIPLE_CHOICE) 테스트")
//  class MultipleChoiceTest {
//    @Test
//    @DisplayName("정상적인 다중 선택 응답은 통과해야 한다")
//    void validateMultipleChoice_ShouldAcceptValidFormat() {
//      assertAll(
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1,2,3")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1, 2, 3")
//          ))
//        )
//      );
//    }
//
//    @Test
//    @DisplayName("잘못된 다중 선택 응답은 예외가 발생해야 한다")
//    void validateMultipleChoice_ShouldRejectInvalidFormat() {
//      assertAll(
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1,3,,4")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1,2,")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1,true,  ",1,2")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1,0,2")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.MULTIPLE_CHOICE, 1, true, "1,abc,2")
//          ))
//        )
//      );
//    }
//  }
//
//  @Nested
//  @DisplayName("주관식(SHORT_ANSWER) 테스트")
//  class ShortAnswerTest {
//    @Test
//    @DisplayName("정상적인 주관식 응답은 통과해야 한다")
//    void validateShortAnswer_ShouldAcceptValidFormat() {
//      assertAll(
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, "테스트 응답입니다")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, "123")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, "!@#$%")
//          ))
//        )
//      );
//    }
//
//    @Test
//    @DisplayName("빈 주관식 응답은 예외가 발생해야 한다")
//    void validateShortAnswer_ShouldRejectInvalidFormat() {
//      assertAll(
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, "")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, " ")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.SHORT_ANSWER, 1, true, "\t\n")
//          ))
//        )
//      );
//    }
//  }
//
//  @Nested
//  @DisplayName("표 선택(TABLE_SELECT) 테스트")
//  class TableSelectTest {
//    @Test
//    @DisplayName("정상적인 표 선택 응답은 통과해야 한다")
//    void validateTableSelect_ShouldAcceptValidFormat() {
//      assertAll(
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:2,2:3,3:1")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:1")
//          ))
//        ),
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:2, 2:3, 3:1")
//          ))
//        )
//      );
//    }
//
//    @Test
//    @DisplayName("잘못된 표 선택 응답은 예외가 발생해야 한다")
//    void validateTableSelect_ShouldRejectInvalidFormat() {
//      assertAll(
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:2,,2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:,2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, ":1,2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:2,")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "0:1,2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "1:0,2:3")
//          ))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponses(List.of(
//            new QuestionResponse("1", QuestionType.TABLE_SELECT, 1, true, "abc:1,2:3")
//          ))
//        )
//      );
//    }
//  }
//
//  @Nested
//  @DisplayName("QuestionResponseUpdate 테스트")
//  class QuestionResponseUpdateTest {
//    @Test
//    @DisplayName("업데이트 요청도 동일한 검증 규칙이 적용되어야 한다")
//    void validateResponseUpdate_ShouldFollowSameRules() {
//      // given
//      QuestionResponseUpdate validUpdate = new QuestionResponseUpdate(
//        "1", QuestionType.SINGLE_CHOICE, true, "1,2,3"
//      );
//      QuestionResponseUpdate invalidUpdate = new QuestionResponseUpdate(
//        "2", QuestionType.SINGLE_CHOICE, true,  "1,,2,3"
//      );
//
//      // when & then
//      assertAll(
//        () -> assertDoesNotThrow(() ->
//          validator.validateResponseUpdates(List.of(validUpdate))
//        ),
//        () -> assertThrows(ResponseException.class, () ->
//          validator.validateResponseUpdates(List.of(invalidUpdate))
//        )
//      );
//    }
//  }
//}