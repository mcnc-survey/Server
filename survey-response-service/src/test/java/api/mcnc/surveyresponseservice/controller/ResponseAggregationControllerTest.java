package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.RestDocsConfig;
import api.mcnc.surveyresponseservice.service.ResponseAggregationService;
import api.mcnc.surveyresponseservice.controller.response.aggregation.ResponseAggregationResponse;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
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

  @Test
  void 설문_ID_별_결과_조회() throws Exception {
    // given
    Map<Integer, Object> groupByOrderNumber = Map.of(
      1, Map.of("3", 3),
      2, Map.of("1", 3, "3", 3, "4", 3),
      3, List.of("전반적으로 만족스러웠습니다.", "전반적으로 만족스러웠습니다.", "전반적으로 만족스러웠습니다."),
      4, Map.of("1", 3, "2", 3, "3", 3, "4", 3, "5", 3)
    );
    ResponseAggregationResponse response = new ResponseAggregationResponse(4, groupByOrderNumber);
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
            fieldWithPath("resultCode").description("결과 코드"),
            fieldWithPath("message").description("결과 메시지"),

            // Body Fields
            fieldWithPath("body.totalResponseCount").description("전체 응답자 수"),
            fieldWithPath("body.groupByOrderNumber").description("문항 순서별 응답 집계 결과"),

            // Question 1 (Single Choice)
            fieldWithPath("body.groupByOrderNumber.1").description("1번 문항 응답 집계 (단일 선택)"),
            fieldWithPath("body.groupByOrderNumber.1.3").description("1번 문항의 3번 선택지 선택 횟수"),

            // Question 2 (Multiple Choice)
            fieldWithPath("body.groupByOrderNumber.2").description("2번 문항 응답 집계 (다중 선택)"),
            fieldWithPath("body.groupByOrderNumber.2.1").description("2번 문항의 1번 선택지 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.2.3").description("2번 문항의 3번 선택지 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.2.4").description("2번 문항의 4번 선택지 선택 횟수"),

            // Question 3 (Short Answer)
            fieldWithPath("body.groupByOrderNumber.3").description("3번 문항 응답 목록 (주관식)"),
            fieldWithPath("body.groupByOrderNumber.3[]").description("3번 문항의 개별 주관식 응답"),

            // Question 4 (Table Select)
            fieldWithPath("body.groupByOrderNumber.4").description("4번 문항 응답 집계 (표 선택)"),
            fieldWithPath("body.groupByOrderNumber.4.1").description("4번 문항의 1번 항목 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.4.2").description("4번 문항의 2번 항목 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.4.3").description("4번 문항의 3번 항목 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.4.4").description("4번 문항의 4번 항목 선택 횟수"),
            fieldWithPath("body.groupByOrderNumber.4.5").description("4번 문항의 5번 항목 선택 횟수")
          )
        )
      )
      .andDo(print());
  }
}