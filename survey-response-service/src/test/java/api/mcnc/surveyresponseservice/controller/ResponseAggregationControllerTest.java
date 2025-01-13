package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.RestDocsConfig;
import api.mcnc.surveyresponseservice.controller.response.aggregation.ResponseAggregationResponse;
import api.mcnc.surveyresponseservice.controller.response.aggregation.ResponseSnippet;
import api.mcnc.surveyresponseservice.controller.response.aggregation.SurveyResultValue;
import api.mcnc.surveyresponseservice.controller.response.aggregation.SurveySummary;
import api.mcnc.surveyresponseservice.service.ResponseAggregationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static api.mcnc.surveyresponseservice.entity.response.QuestionType.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-18 오전 10:36
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
class ResponseAggregationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ResponseAggregationService responseService;

  private ResponseAggregationResponse getResponseAggregationResponse() {
    SurveySummary surveySummary = SurveySummary.of("우당탕탕 설문 조사", 2, "2024-12-26T13:56", "2024-12-09");
    List<Object> responses = List.of(
      ResponseSnippet.of("만족", 1),
      ResponseSnippet.of("매우 만족", 2)
    );
    List<Object> responses2 = List.of(
      ResponseSnippet.of("피자", 1)
    );
    List<Object> responses3 = List.of(
      "집에 가고 싶다네요"
    );

    Map<Integer, SurveyResultValue> surveyResults = Map.of(
      1, new SurveyResultValue("86cddc4d-6ae3-451f-8d65-95d43de48ddf", "서비스에 만족하셨습니까?", SINGLE_CHOICE, 2, 2, responses, 0, List.of()),
      2, new SurveyResultValue("ede654e3-0f1b-47bd-a930-89548b8cbfac", "좋아하는 메뉴를 골라주세요", MULTIPLE_CHOICE, 2, 1, responses2, 1, List.of("짜장면")),
      3, new SurveyResultValue("e4a10b03-955b-4705-8422-2045aadba5e6", "하고싶은 말을 적어주세요", SHORT_ANSWER, 2, 1, responses3, 1, List.of("아이고 내 팔자야"))
    );
    return new ResponseAggregationResponse(surveySummary, surveyResults);
  }

  @Test
  void 설문_ID_별_결과_조회() throws Exception {
    // given
    ResponseAggregationResponse response = getResponseAggregationResponse();
    given(responseService.getResponseAggregationBySurveyId(anyString()))
      .willReturn(response);

    // when & then
    mockMvc.perform(
        RestDocumentationRequestBuilders.get("/responses-aggregation/{surveyId}", "{surveyId}")
      )
      .andExpect(status().isOk())
      .andDo(
        document("get-response-aggregation",
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          ),
          responseFields(
            // Common Response Fields
            fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
            fieldWithPath("resultCode").type(STRING).description("결과 코드"),
            fieldWithPath("message").type(STRING).description("결과 메시지"),
            fieldWithPath("body").type(OBJECT).description("결과 메시지"),
            fieldWithPath("body.surveySummary").type(OBJECT).description("결과 요약"),
            fieldWithPath("body.surveySummary.title").type(STRING).description("설문 이름"),
            fieldWithPath("body.surveySummary.respondentCount").type(NUMBER).description("총 응답자 수"),
            fieldWithPath("body.surveySummary.endDate").type(STRING).description("설문 종료 날짜"),
            fieldWithPath("body.surveySummary.lastModifiedDate").type(STRING).description("마지막 수정일"),

            fieldWithPath("body.surveyResults").type(OBJECT).description("결과 메시지"),
            fieldWithPath("body.surveyResults.1").type(OBJECT).description("1번 질문"),
            fieldWithPath("body.surveyResults.1.questionId").type(STRING).description("질문 ID"),
            fieldWithPath("body.surveyResults.1.questionTitle").type(STRING).description("질문 제목"),
            fieldWithPath("body.surveyResults.1.questionType").type(STRING).description("질문 유형"),
            fieldWithPath("body.surveyResults.1.totalResponseCount").type(NUMBER).description("질문에 대한 총 응답자 수"),
            fieldWithPath("body.surveyResults.1.responseCount").type(NUMBER).description("일반 응답 수"),
            fieldWithPath("body.surveyResults.1.responses").type(ARRAY).description("질문에 대한 응답"),
            fieldWithPath("body.surveyResults.1.responses[].text").type(STRING).description("응답 텍스트"),
            fieldWithPath("body.surveyResults.1.responses[].count").type(NUMBER).description("응답 수"),
            fieldWithPath("body.surveyResults.1.etcCount").type(NUMBER).description("기타 응답 수"),
            fieldWithPath("body.surveyResults.1.etc").type(ARRAY).description("기타 항목 리스트"),

            // Question 2
            fieldWithPath("body.surveyResults.2").type(OBJECT).description("2번 질문"),
            fieldWithPath("body.surveyResults.2.questionId").type(STRING).description("질문 ID"),
            fieldWithPath("body.surveyResults.2.questionTitle").type(STRING).description("질문 제목"),
            fieldWithPath("body.surveyResults.2.questionType").type(STRING).description("질문 유형"),
            fieldWithPath("body.surveyResults.2.totalResponseCount").type(NUMBER).description("질문에 대한 총 응답자 수"),
            fieldWithPath("body.surveyResults.2.responseCount").type(NUMBER).description("일반 응답 수"),
            fieldWithPath("body.surveyResults.2.responses").type(ARRAY).description("질문에 대한 응답 배열"),
            fieldWithPath("body.surveyResults.2.responses[].text").type(STRING).description("응답 텍스트"),
            fieldWithPath("body.surveyResults.2.responses[].count").type(NUMBER).description("응답 수"),
            fieldWithPath("body.surveyResults.2.etcCount").type(NUMBER).description("기타 응답 수"),
            fieldWithPath("body.surveyResults.2.etc").type(ARRAY).description("기타 항목 리스트"),

            // Question 3
            fieldWithPath("body.surveyResults.3").type(OBJECT).description("3번 질문"),
            fieldWithPath("body.surveyResults.3.questionId").type(STRING).description("질문 ID"),
            fieldWithPath("body.surveyResults.3.questionTitle").type(STRING).description("질문 제목"),
            fieldWithPath("body.surveyResults.3.questionType").type(STRING).description("질문 유형"),
            fieldWithPath("body.surveyResults.3.totalResponseCount").type(NUMBER).description("질문에 대한 총 응답자 수"),
            fieldWithPath("body.surveyResults.3.responseCount").type(NUMBER).description("일반 응답 수"),
            fieldWithPath("body.surveyResults.3.responses").type(ARRAY).description("질문에 대한 응답 배열 (자유 응답 텍스트)"),
            fieldWithPath("body.surveyResults.3.etcCount").type(NUMBER).description("기타 응답 수"),
            fieldWithPath("body.surveyResults.3.etc").type(ARRAY).description("기타 항목 리스트")

          )
        )
      )
      .andDo(print());
  }
}