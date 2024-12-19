package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.RestDocsConfig;
import api.mcnc.surveyresponseservice.client.survey.response.Question;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponse;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.controller.request.ResponseSaveRequest;
import api.mcnc.surveyresponseservice.controller.request.ResponseUpdateRequest;
import api.mcnc.surveyresponseservice.controller.response.ResponseResult;
import api.mcnc.surveyresponseservice.controller.response.SurveyResponsesResponse;
import api.mcnc.surveyresponseservice.controller.response.SurveySnippet;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import api.mcnc.surveyresponseservice.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.AutoConfigureJooq;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static api.mcnc.surveyresponseservice.entity.response.QuestionType.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-15 오전 10:11
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
class ResponseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ResponseService responseService;

  @Test
  void 사용자_응답_조회() throws Exception {
    // given
    SurveySnippet snippet = SurveySnippet.builder()
      .id("42cec33e-4473-4241-b311-1a533106e8fb")
      .title("고객 만족도 조사5")
      .description("서비스에 대한 피드백을 받고자 설문을 진행합니다.")
      .question(
        List.of(Question.builder()
            .id("3af1fbc5-4c4f-4fd1-bf48-15788c0eadfb")
              .title("서비스에 만족하셨습니까?")
            .questionType(SINGLE_CHOICE)
            .order(1)
            .columns("매우 불만족|`|불만족|`|보통|`|만족|`|매우 만족")
            .required(true)
            .etc(false)
          .build())
      )
      .startDateTime("2024-12-09 17:51")
      .endDateTime("2024-12-09 17:51")
      .build();

    Map<Integer, ResponseResult> response = Map.of(1, ResponseResult.builder().id("3af1fbc5-4c4f-4fd1-bf48-15788c0eadfb").response("만족").build());
    SurveyResponsesResponse surveyResponsesResponse = SurveyResponsesResponse.of(snippet, response);
    given(responseService.getAllMyResponseResults(anyString()))
      .willReturn(surveyResponsesResponse);

    mockMvc.perform(
        RestDocumentationRequestBuilders.get("/responses/{surveyId}", "{surveyId}")
          .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNzEwYjgxYi03OTMzLTQwZTMtYjZiMi05MGZmYjhjZmIyYmEiLCJzdXJ2ZXlJZCI6IjI1OGYyODQ0LWEyZDktNDRhZi1hMDU4LTAyYjE5OTYxNTY1NiIsIm5hbWUiOiLsnKDtnazspIAiLCJlbWFpbCI6InloajM4NTVAbmF2ZXIuY29tIiwiaWF0IjoxNzMyNzY4OTE4LCJleHAiOjE3MzI3Njg5MTl9.nZzeYTpMWI0nE7fhQOFlc6BQ9hLZzfDSGBmqMlvX1P19ikpmPN5UFvdlOsaH_JCoJDE1QN7EvN6MILHE9ki7Yg")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(
        document("getAllResponseByRespondent",
          requestHeaders(
            headerWithName("Authorization").description("응답자 jwt 토큰")
          ),
          responseFields(
            fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body").type(OBJECT).description("응답 데이터"),
            fieldWithPath("body.surveySnippet").type(OBJECT).description("설문 정보"),
            fieldWithPath("body.surveySnippet.id").type(STRING).description("설문 아이디"),
            fieldWithPath("body.surveySnippet.title").type(STRING).description("설문 제목"),
            fieldWithPath("body.surveySnippet.description").type(STRING).description("설문 설명"),
            fieldWithPath("body.surveySnippet.question").type(ARRAY).description("설문의 질문 정보"),
            fieldWithPath("body.surveySnippet.question[].id").type(STRING).description("질문 아이디"),
            fieldWithPath("body.surveySnippet.question[].title").type(STRING).description("질문 제목"),
            fieldWithPath("body.surveySnippet.question[].questionType").type(STRING).description("질문 유형"),
            fieldWithPath("body.surveySnippet.question[].order").type(NUMBER).description("질문 순서"),
            fieldWithPath("body.surveySnippet.question[].columns").type(ARRAY).description("질문 선택 컬럼 명들"),
            fieldWithPath("body.surveySnippet.question[].required").type(BOOLEAN).description("필수 여부"),
            fieldWithPath("body.surveySnippet.question[].etc").type(BOOLEAN).description("기타 항목 선택 가능 여부"),
            fieldWithPath("body.surveySnippet.startDateTime").type(STRING).description("설문 시작 날짜"),
            fieldWithPath("body.surveySnippet.endDateTime").type(STRING).description("설문 종료 날짜"),
            fieldWithPath("body.responseResult").type(OBJECT).description("설문에 대한 나의 응답 정보"),
            fieldWithPath("body.responseResult.1").type(OBJECT).description("1번 항목에 대한 정보"),
            fieldWithPath("body.responseResult.1.id").type(STRING).description("1번 질문의 아이디"),
            fieldWithPath("body.responseResult.1.response").type(STRING).description("1번 질문의 나의 응답")

          ),
          pathParameters(
            parameterWithName("surveyId").description("설문 ID")
          )
        )
      )
      .andDo(print())
    ;

  }


  @Test
  void 사용자_응답_저장() throws Exception {
    // Given: Mock된 Service와 필요 데이터 설정
    doNothing().when(responseService).setResponse(anyString(), anyList());

    QuestionResponse qr1 = new QuestionResponse("uuid-32", SINGLE_CHOICE, 1, true, "그렇다");
    QuestionResponse qr2 = new QuestionResponse("uuid-32", MULTIPLE_CHOICE, 2, true, "피자|`|치킨");
    QuestionResponse qr3 = new QuestionResponse("uuid-32", SHORT_ANSWER, 3, true, "집에 가고 싶다");
    QuestionResponse qr4 = new QuestionResponse("uuid-32", LONG_ANSWER, 4, true, "집에 가고 싶다 라고 말하면 집에 갈 수 있다는 것에 대해서 말하다 보면 언젠가 집에 가지 않을까 하는 생각");

    ResponseSaveRequest request = new ResponseSaveRequest(List.of(qr1, qr2, qr3, qr4));

    // When: MockMvc를 통한 요청 수행
    mockMvc.perform(
        RestDocumentationRequestBuilders.post("/responses/{surveyId}", "{surveyId}")
          .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNzEwYjgxYi03OTMzLTQwZTMtYjZiMi05MGZmYjhjZmIyYmEiLCJzdXJ2ZXlJZCI6IjI1OGYyODQ0LWEyZDktNDRhZi1hMDU4LTAyYjE5OTYxNTY1NiIsIm5hbWUiOiLsnKDtnazspIAiLCJlbWFpbCI6InloajM4NTVAbmF2ZXIuY29tIiwiaWF0IjoxNzMyNzY4OTE4LCJleHAiOjE3MzI3Njg5MTl9.nZzeYTpMWI0nE7fhQOFlc6BQ9hLZzfDSGBmqMlvX1P19ikpmPN5UFvdlOsaH_JCoJDE1QN7EvN6MILHE9ki7Yg")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andDo(
        document("saveResponse",
          requestHeaders(
            headerWithName("Authorization").description("응답자 jwt 토큰")
          ),
          requestFields(
            fieldWithPath("responses").type(ARRAY).description("응답 목록"),
            fieldWithPath("responses[].questionId").type(STRING).description("질문 ID"),
            fieldWithPath("responses[].questionType").type(STRING).description("질문 유형"),
            fieldWithPath("responses[].orderNumber").type(NUMBER).description("질문 순서"),
            fieldWithPath("responses[].isRequired").type(BOOLEAN).description("필수 여부"),
            fieldWithPath("responses[].response").type(STRING).description("답변")
          ),
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          )
        )
      )
      .andDo(print());

    // Verify: 서비스 메서드 호출 확인
    verify(responseService, times(1)).setResponse(eq("{surveyId}"), anyList());
  }

  @Test
  void 사용자_응답_수정() throws Exception {
    // given: Mock된 서비스와 데이터 설정
    ResponseUpdateRequest updateRequest = new ResponseUpdateRequest(
      List.of(
        new QuestionResponseUpdate("87494fba-cc90-4a8f-a38a-4744664c3bea", true, "아니다"),
        new QuestionResponseUpdate("87494fba-cc90-5a8f-b48c-7743644c351a", true, "탕수육")
      )
    );
    doNothing().when(responseService).updateResponse(anyString(), anyList());

    // When: MockMvc를 통한 요청 수행
    mockMvc.perform(
        RestDocumentationRequestBuilders.put("/responses/{surveyId}", "{surveyId}")
          .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNzEwYjgxYi03OTMzLTQwZTMtYjZiMi05MGZmYjhjZmIyYmEiLCJzdXJ2ZXlJZCI6IjI1OGYyODQ0LWEyZDktNDRhZi1hMDU4LTAyYjE5OTYxNTY1NiIsIm5hbWUiOiLsnKDtnazspIAiLCJlbWFpbCI6InloajM4NTVAbmF2ZXIuY29tIiwiaWF0IjoxNzMyNzY4OTE4LCJleHAiOjE3MzI3Njg5MTl9.nZzeYTpMWI0nE7fhQOFlc6BQ9hLZzfDSGBmqMlvX1P19ikpmPN5UFvdlOsaH_JCoJDE1QN7EvN6MILHE9ki7Yg")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(updateRequest)
          )
      )
      .andExpect(status().isOk())
      .andDo(
        document("updateResponse",
          requestHeaders(
            headerWithName("Authorization").description("응답자 jwt 토큰")
          ),
          requestFields(
            fieldWithPath("responses").type(ARRAY).description("응답 수정 목록"),
            fieldWithPath("responses[].id").type(STRING).description("응답 ID"),
            fieldWithPath("responses[].isRequired").type(BOOLEAN).description("필수 여부"),
            fieldWithPath("responses[].response").type(STRING).description("수정된 답변")
          ),
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          )
        )
      )
      .andDo(print());

    // Verify: 서비스 메서드 호출 확인
    verify(responseService, times(1)).updateResponse(eq("{surveyId}"), anyList());
  }



}

