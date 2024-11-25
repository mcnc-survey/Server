package api.mcnc.surveyservice.controller;

import api.mcnc.surveyservice.RestDocsConfig;
import api.mcnc.surveyservice.common.audit.interceptor.RequestedByInterceptor;
import api.mcnc.surveyservice.controller.request.QuestionCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyCreateRequest;
import api.mcnc.surveyservice.controller.request.SurveyUpdateRequest;
import api.mcnc.surveyservice.controller.response.QuestionDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyCalendarResponse;
import api.mcnc.surveyservice.controller.response.SurveyDetailsResponse;
import api.mcnc.surveyservice.controller.response.SurveyResponse;
import api.mcnc.surveyservice.entity.question.QuestionType;
import api.mcnc.surveyservice.entity.survey.SurveyStatus;
import api.mcnc.surveyservice.service.survey.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 설문_생성() throws Exception {
    doNothing().when(surveyService).setSurveyAndQuestions(any(SurveyCreateRequest.class));
    LocalDateTime now = LocalDateTime.now().plusDays(1);
    SurveyCreateRequest 고객_만족도_조사 =
      new SurveyCreateRequest(
        "고객 만족도 조사",
        "서비스에 대한 피드백을 받고자 설문을 진행합니다.",
        now,
        now.plusDays(3),
        List.of(
          new QuestionCreateRequest("서비스에 만족하셨습니까?", QuestionType.SINGLE_CHOICE, 1, "매우 불만족,불만족,보통,만족,매우 만족", null),
          new QuestionCreateRequest("추가로 개선이 필요한 부분은 무엇인가요?", QuestionType.SHORT_ANSWER, 2, null, null),
          new QuestionCreateRequest("서비스 개선 항목을 선택해주세요", QuestionType.MULTIPLE_CHOICE, 3, "가격,품질,고객 지원,기타", null),
          new QuestionCreateRequest("서비스 품질 평가", QuestionType.TABLE_SELECT, 4, "매우 나쁨,나쁨,보통,좋음,매우 좋음", "응답 속도,친절도,문제 해결 능력")
        )
      );


    mockMvc.perform(
        post("/surveys")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(고객_만족도_조사))
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
        .status(SurveyStatus.ON)
        .lastModifiedAt("2024-11-20T13:56:00")
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
            fieldWithPath("body[].status").type(STRING).description("설문 상태 (ON, WAIT, END)"),
            fieldWithPath("body[].lastModifiedAt").type(STRING).description("설문 종료일 (ISO-8601 형식, 예: 2024-11-20T13:56:00)")
          )
        )
      )
      .andDo(print());

  }

  @Test
  void 수정할_설문_상세_보기() throws Exception {
    SurveyDetailsResponse surveyDetailsResponse = new SurveyDetailsResponse(
      "5ef3c3cf-329a-46bf-801b-e2fef6d9f339",
      "Customer Satisfaction Survey",
      "This survey aims to collect feedback on customer satisfaction.",
      "2024-11-26T09:00",
      "2024-11-30T18:00",
      List.of(
        QuestionDetailsResponse.builder().id("3220c2f8-92f5-40ba-991f-0b07a3c361bc").title("만족도").questionType(QuestionType.SINGLE_CHOICE).order(1).columns("매우 불만족,불만족,보통,만족,매우 만족").rows(null).build(),
        QuestionDetailsResponse.builder().id("3220c2f8-92f5-40ba-991f-0b07a3c361bc").title("후기를 남겨주세요").questionType(QuestionType.SHORT_ANSWER).order(1).columns(null).rows(null).build()
      )
    );

    given(surveyService.getDetail("5ef3c3cf-329a-46bf-801b-e2fef6d9f339")).willReturn(surveyDetailsResponse);

    mockMvc.perform(get("/surveys/survey-id/{surveyId}/edit", "5ef3c3cf-329a-46bf-801b-e2fef6d9f339"))
      .andExpect(status().isOk())
      .andDo(
        document("getSurveyDetail", // RestDocs 문서화 작업
          pathParameters(
            parameterWithName("surveyId").description("설문 ID")
          ),
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body").type(OBJECT).description("설문 목록"),
            fieldWithPath("body.id").type(STRING).description("설문 ID"),
            fieldWithPath("body.title").type(STRING).description("설문 제목"),
            fieldWithPath("body.description").type(STRING).description("설문 설명").optional(),
            fieldWithPath("body.startAt").type(STRING).description("설문 시작일"),
            fieldWithPath("body.endAt").type(STRING).description("설문 종료일"),
            fieldWithPath("body.question[]").type(ARRAY).description("설문 항목 리스트"),
            fieldWithPath("body.question[].id").type(STRING).description("설문 항목 아이디"),
            fieldWithPath("body.question[].title").type(STRING).description("설문 항목 제목"),
            fieldWithPath("body.question[].questionType").type(STRING).description("설문 항목 타입 (SINGLE_CHOICE, MULTIPLE_CHOICE, SHORT_ANSWER, TABLE_SELECT 등)"),
            fieldWithPath("body.question[].order").type(NUMBER).description("설문 항목 순서"),
            fieldWithPath("body.question[].columns").type(STRING).description("항목에 대한 선택 항목 (콤마로 구분된 문자열)").optional(),
            fieldWithPath("body.question[].rows").type(STRING).description("표 형식 항목에서 행 항목 (콤마로 구분된 문자열)").optional()
          )
        )
      )
      .andDo(print());
  }

  @Test
  void 설문_수정() throws Exception {
    doNothing().when(surveyService).updateSurvey(anyString(), any(SurveyUpdateRequest.class));
    LocalDateTime now = LocalDateTime.now().plusDays(1);
    SurveyUpdateRequest surveyUpdateRequest = new SurveyUpdateRequest(
      "Customer Satisfaction Survey",
      "This survey aims to collect feedback on customer satisfaction.",
      now,
      now.plusDays(3),
      List.of(
        new SurveyUpdateRequest.Question(null, "만족도", QuestionType.SINGLE_CHOICE, 1, "매우 불만족, 불만족, 보통, 만족, 매우 만족", null),
        new SurveyUpdateRequest.Question("157020b2-0009-4858-9bcb-b2c30e091514", "후기를 남겨주세요", QuestionType.SHORT_ANSWER, 2, null, null)
      )
    );


    mockMvc.perform(
        put("/surveys/survey-id/{surveyId}", "5ef3c3cf-329a-46bf-801b-e2fef6d9f339")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(surveyUpdateRequest))
      )
      .andExpect(status().isOk())
      .andDo(
        document("updateSurvey", // RestDocs 문서화 작업
          requestFields(
            fieldWithPath("title").type(STRING).description("설문 제목"),
            fieldWithPath("description").type(STRING).description("설문 설명").optional(),
            fieldWithPath("startAt").type(STRING).description("설문 시작일 (ISO-8601 형식, 예: 2024-11-20T13:55:00)"),
            fieldWithPath("endAt").type(STRING).description("설문 종료일 (ISO-8601 형식, 예: 2024-11-20T13:56:00)"),
            fieldWithPath("updateQuestionList").type(ARRAY).description("설문 질문 목록"),
            fieldWithPath("updateQuestionList[].id").type(STRING).description("설문 항목 아이디").optional(),
            fieldWithPath("updateQuestionList[].title").type(STRING).description("항목 제목"),
            fieldWithPath("updateQuestionList[].questionType").type(STRING).description("항목 타입 (SINGLE_CHOICE, MULTIPLE_CHOICE, SHORT_ANSWER, TABLE_SELECT 등)"),
            fieldWithPath("updateQuestionList[].order").type(NUMBER).description("항목 순서"),
            fieldWithPath("updateQuestionList[].columns").type(STRING).optional().description("항목에 대한 선택 항목 (콤마로 구분된 문자열)").optional(),
            fieldWithPath("updateQuestionList[].rows").type(STRING).optional().description("표 형식 항목에서 행 항목 (콤마로 구분된 문자열)").optional()
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
  void 설문_삭제() throws Exception {

    doNothing().when(surveyService).deleteSurvey(anyString());

    mockMvc.perform(delete("/surveys/survey-id/{surveyId}", "5ef3c3cf-329a-46bf-801b-e2fef6d9f339"))
      .andExpect(status().isOk())
      .andDo(
        document("deleteSurvey", // RestDocs 문서화 작업
          pathParameters(
            parameterWithName("surveyId").description("설문 ID")
          ),
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지")
          )
        )
      )
      .andDo(print());
  }

  @Test
  void 삭제된_항목_조회() throws Exception {

    List<SurveyResponse> surveyResponse =
      List.of(SurveyResponse.builder()
        .id("258f2844-a2d9-44af-a058-02b199615656")
        .title("고객 만족도 조사")
        .status(SurveyStatus.DELETE)
        .lastModifiedAt("2024-11-25T15:36:00")
        .build());
    given(surveyService.getSurveyListForDelete()).willReturn(surveyResponse);


    mockMvc.perform(get("/surveys/delete"))
      .andExpect(status().isOk())
      .andDo(
        document("getSurveyListForDelete", // RestDocs 문서화 작업
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body[]").type(ARRAY).description("설문 목록"),
            fieldWithPath("body[].id").type(STRING).description("설문 ID"),
            fieldWithPath("body[].title").type(STRING).description("설문 제목"),
            fieldWithPath("body[].status").type(STRING).description("설문 상태 (ON, WAIT, END)"),
            fieldWithPath("body[].lastModifiedAt").type(STRING).description("설문 종료일 (ISO-8601 형식, 예: 2024-11-20T13:56:00)")
          )
        )
      )
      .andDo(print());
  }

  @Test
  void 캘린더_용_항목_조회() throws Exception {

    List<SurveyCalendarResponse> surveyResponse =
      List.of(SurveyCalendarResponse.builder()
        .id("258f2844-a2d9-44af-a058-02b199615656")
        .title("고객 만족도 조사")
        .startAt("2024-11-25T15:36:00")
        .endAt("2024-12-25T15:36:00")
        .build());
    given(surveyService.getSurveyListForCalendar()).willReturn(surveyResponse);


    mockMvc.perform(get("/surveys/calendar"))
      .andExpect(status().isOk())
      .andDo(
        document("getSurveyListForCalendar", // RestDocs 문서화 작업
          responseFields(
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body[]").type(ARRAY).description("설문 목록"),
            fieldWithPath("body[].id").type(STRING).description("설문 ID"),
            fieldWithPath("body[].title").type(STRING).description("설문 제목"),
            fieldWithPath("body[].startAt").type(STRING).description("설문 시작일"),
            fieldWithPath("body[].endAt").type(STRING).description("설문 종료일")
          )
        )
      )
      .andDo(print());
  }

}