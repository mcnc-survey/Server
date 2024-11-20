package api.mcnc.surveyservice.controller;

import api.mcnc.surveyservice.RestDocsConfig;
import api.mcnc.surveyservice.common.audit.interceptor.RequestedByInterceptor;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import api.mcnc.surveyservice.service.survey.SurveyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 2:26
 */
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@WebMvcTest(SurveyController.class) // 테스트할 Controller만 로드
@DisplayName("설문 테스트")
class SurveyControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SurveyService surveyService;

  @MockBean
  private RequestedByInterceptor requestedByInterceptor;

  @Test
  void 설문_생성() throws Exception {
    doNothing().when(surveyService).setSurveyAndQuestions(any(SurveyCreateRequest.class));

    mockMvc.perform(
      post("/surveys")
        .contentType(APPLICATION_JSON)
        .content(
          """
            {
                "title": "고객 만족도 조사",
                "description": "서비스에 대한 피드백을 받고자 설문을 진행합니다.",
                "startAt": "2024-11-22T13:55:00",
                "endAt": "2024-11-24T13:56:00",
                "questions": [
                    {
                        "title": "서비스에 만족하셨습니까?",
                        "questionType": "SINGLE_CHOICE",
                        "order": 1,
                        "columns": "매우 불만족,불만족,보통,만족,매우 만족",
                        "rows": null
                    },
                    {
                        "title": "추가로 개선이 필요한 부분은 무엇인가요?",
                        "questionType": "SHORT_ANSWER",
                        "order": 2,
                        "columns": null,
                        "rows": null
                    },
                    {
                        "title": "서비스 개선 항목을 선택해주세요",
                        "questionType": "MULTIPLE_CHOICE",
                        "order": 3,
                        "columns": "가격,품질,고객 지원,기타",
                        "rows": null
                    },
                    {
                        "title": "서비스 품질 평가",
                        "questionType": "TABLE_SELECT",
                        "order": 4,
                        "columns": "매우 나쁨,나쁨,보통,좋음,매우 좋음",
                        "rows": "응답 속도,친절도,문제 해결 능력"
                    }
                ]
            }
          """
        )
    )
      .andExpect(status().isOk())
      .andDo(
        document("saveSurvey", // RestDocs 문서화 작업
          requestFields(
            fieldWithPath("title").type(STRING).description("설문 제목"),
            fieldWithPath("description").type(STRING).description("설문 설명"),
            fieldWithPath("startAt").type(STRING).description("설문 시작일 (ISO-8601 형식, 예: 2024-11-20T13:55:00)"),
            fieldWithPath("endAt").type(STRING).description("설문 종료일 (ISO-8601 형식, 예: 2024-11-20T13:56:00)"),
            fieldWithPath("questions").type(ARRAY).description("설문 질문 목록"),
            fieldWithPath("questions[].title").type(STRING).description("질문 제목"),
            fieldWithPath("questions[].questionType").type(STRING).description("질문 타입 (SINGLE_CHOICE, MULTIPLE_CHOICE, SHORT_ANSWER, TABLE_SELECT 등)"),
            fieldWithPath("questions[].order").type(NUMBER).description("질문 순서"),
            fieldWithPath("questions[].columns").type(STRING).optional().description("질문에 대한 선택 항목 (콤마로 구분된 문자열)"),
            fieldWithPath("questions[].rows").type(STRING).optional().description("표 형식 질문에서 행 항목 (콤마로 구분된 문자열)")
          ),
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지")
          )
        )
      )
      .andDo(print())
    ;
  }

  @Test
  void 내가_만든_설문_조회() throws Exception {
    List<SurveyResponse> surveyResponses = List.of(
      SurveyResponse.builder()
        .id("7a762f88-0aa9-48b0-8a12-fc680523cd67")
        .title("고객 만족도 조사")
        .description("서비스에 대한 피드백을 받고자 설문을 진행합니다.")
        .status(SurveyStatus.ON)
        .startAt("2024-11-20T13:55:00")
        .endAt("2024-11-20T13:56:00")
        .build()
    );

    given(surveyService.getSurveyList()).willReturn(surveyResponses);

    mockMvc.perform(get("/surveys"))
      .andExpect(status().isOk())
      .andDo(
        document("getSurveyList", // RestDocs 문서화 작업
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body[]").type(ARRAY).description("설문 목록"),
            fieldWithPath("body[].id").type(STRING).description("설문 ID"),
            fieldWithPath("body[].title").type(STRING).description("설문 제목"),
            fieldWithPath("body[].description").type(STRING).description("설문 설명"),
            fieldWithPath("body[].status").type(STRING).description("설문 상태 (ON, WAIT, END)"),
            fieldWithPath("body[].startAt").type(STRING).description("설문 시작일 (ISO-8601 형식, 예: 2024-11-20T13:55:00)"),
            fieldWithPath("body[].endAt").type(STRING).description("설문 종료일 (ISO-8601 형식, 예: 2024-11-20T13:56:00)")
          )
        )
      )
      .andDo(print());

  }
}