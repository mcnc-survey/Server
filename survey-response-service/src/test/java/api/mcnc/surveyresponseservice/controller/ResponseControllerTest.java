package api.mcnc.surveyresponseservice.controller;

import api.mcnc.surveyresponseservice.RestDocsConfig;
import api.mcnc.surveyresponseservice.controller.request.QuestionResponseUpdate;
import api.mcnc.surveyresponseservice.controller.request.ResponseUpdateRequest;
import api.mcnc.surveyresponseservice.controller.response.ResponseResult;
import api.mcnc.surveyresponseservice.entity.response.QuestionType;
import api.mcnc.surveyresponseservice.service.ResponseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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

  @MockBean
  private ResponseService responseService;

  @Test
  void 사용자_응답_조회() throws Exception {
    // given
    ResponseResult response = ResponseResult.builder().id("uuid-32").orderNumber(1).response("test result").build();
    given(responseService.getAllMyResponseResults(anyString(), anyString()))
      .willReturn(List.of(response));

    mockMvc.perform(
        RestDocumentationRequestBuilders.get("/responses/{surveyId}", "uuid-32")
          .param("respondentId", "uuid-32")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(
        document("getAllResponseByRespondent",
          responseFields(
            fieldWithPath("resultCode").type(JsonFieldType.STRING).description("응답 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
            fieldWithPath("body").type(JsonFieldType.ARRAY).description("응답 데이터"),
            fieldWithPath("body[].id").type(JsonFieldType.STRING).description("아이디"),
            fieldWithPath("body[].orderNumber").type(JsonFieldType.NUMBER).description("응답 순번"),
            fieldWithPath("body[].response").type(JsonFieldType.STRING).description("답변")
          ),
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          ),
          queryParameters(
            parameterWithName("respondentId").description("응답자 ID")
          )
        )
      )
      .andDo(print())
    ;

  }


  @Test
  void 사용자_응답_저장() throws Exception {
    // Given: Mock된 Service와 필요 데이터 설정
    doNothing().when(responseService).setResponse(anyString(), anyString(), anyList());

    // When: MockMvc를 통한 요청 수행
    mockMvc.perform(
        RestDocumentationRequestBuilders.post("/responses/{surveyId}", "uuid-32")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            "{\"respondentId\": \"uuid-32\", \"responses\": [" +
              "{\"questionId\": \"11\", \"questionType\": \"SINGLE_CHOICE\", \"orderNumber\": 1, \"response\": \"1\"}]}"
          )
      )
      .andExpect(status().isOk())
      .andDo(
        document("saveResponse",
          requestFields(
            fieldWithPath("respondentId").type(JsonFieldType.STRING).description("응답자 ID"),
            fieldWithPath("responses").type(JsonFieldType.ARRAY).description("응답 목록"),
            fieldWithPath("responses[].questionId").type(JsonFieldType.STRING).description("질문 ID"),
            fieldWithPath("responses[].questionType").type(JsonFieldType.STRING).description("질문 유형"),
            fieldWithPath("responses[].orderNumber").type(JsonFieldType.NUMBER).description("질문 순서"),
            fieldWithPath("responses[].response").type(JsonFieldType.STRING).description("답변")
          ),
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          )
        )
      )
      .andDo(print());

    // Verify: 서비스 메서드 호출 확인
    verify(responseService, times(1)).setResponse(eq("uuid-32"), eq("uuid-32"), anyList());
  }

  @Test
  void 사용자_응답_수정() throws Exception {
    // given: Mock된 서비스와 데이터 설정
    ResponseUpdateRequest updateRequest = new ResponseUpdateRequest(
      "uuid-32",
      List.of(new QuestionResponseUpdate("87494fba-cc90-4a8f-a38a-4744664c3bea", QuestionType.SINGLE_CHOICE, "4"))
    );
    doNothing().when(responseService).updateResponse(anyString(), anyString(), anyList());

    // When: MockMvc를 통한 요청 수행
    mockMvc.perform(
        RestDocumentationRequestBuilders.put("/responses/{surveyId}", "uuid-32")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            "{\"respondentId\": \"uuid-32\", \"responses\": [{\"id\": \"87494fba-cc90-4a8f-a38a-4744664c3bea\", \"questionType\": \"SINGLE_CHOICE\", \"response\": \"4\"}]}"
          )
      )
      .andExpect(status().isOk())
      .andDo(
        document("updateResponse",
          requestFields(
            fieldWithPath("respondentId").type(JsonFieldType.STRING).description("응답자 ID"),
            fieldWithPath("responses").type(JsonFieldType.ARRAY).description("응답 수정 목록"),
            fieldWithPath("responses[].id").type(JsonFieldType.STRING).description("응답 ID"),
            fieldWithPath("responses[].questionType").type(JsonFieldType.STRING).description("질문 유형"),
            fieldWithPath("responses[].response").type(JsonFieldType.STRING).description("수정된 답변")
          ),
          pathParameters(
            parameterWithName("surveyId").description("설문지 ID")
          )
        )
      )
      .andDo(print());

    // Verify: 서비스 메서드 호출 확인
    verify(responseService, times(1)).updateResponse(eq("uuid-32"), eq("uuid-32"), anyList());
  }



}

