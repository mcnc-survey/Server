package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.RestDocsConfig;
import api.mcnc.surveyrespondentservice.authentication.auth.EmailUserInfo;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.service.auth.AuthService;
import api.mcnc.surveyrespondentservice.service.auth.OAuthUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RegisterUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RespondentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import static api.mcnc.surveyrespondentservice.common.constant.ProviderConstant.DEFAULT_PROVIDER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오전 9:27
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
class OAuthControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  AuthService authService;
  @MockBean
  RespondentService respondentService;

  @Autowired
  OAuthUseCase oauthUseCase;

  @Autowired
  RegisterUseCase registerUseCase;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 일반_인증() throws Exception {
    EmailUserInfo request = new EmailUserInfo("example@example.con", "홍길동", "01012345678");
    String param = "c3VydmV5SWQ=";

    Token token = new Token("access token");
    when(registerUseCase.registerRespondent(any(AuthenticatedRespondent.class), anyString())).thenReturn(token);

    mvc.perform(
        post("/auth/sign")
          .queryParam("t", param)
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(request))
      ).andExpect(status().isOk())
      .andDo(
        document(
          "email sign",
          queryParameters(
            parameterWithName("t").description("base64로 인코딩된 설문 id")
          ),
          requestFields(
            fieldWithPath("email").type(STRING).description("email"),
            fieldWithPath("name").type(STRING).description("name"),
            fieldWithPath("phoneNumber").type(STRING).description("phone")
          ),
          responseFields(
            fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body").type(OBJECT).description("응답 데이터"),
            fieldWithPath("body.accessToken").type(STRING).description("access token")
          )
        )
      )
      .andDo(print());

  }

  @Test
  void OAuth2_테스트() throws Exception {
    mvc.perform(get("/oauth2/authorization/{social}", "kakao"))
      .andExpect(status().is3xxRedirection())
      .andDo(document("oauth2",
        pathParameters(
          parameterWithName("social").description("소셜 로그인 타입 - kakao, google, naver")
        )
      ));
  }

}