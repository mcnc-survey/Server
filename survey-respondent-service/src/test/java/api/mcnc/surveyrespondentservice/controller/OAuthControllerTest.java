package api.mcnc.surveyrespondentservice.controller;

import api.mcnc.surveyrespondentservice.RestDocsConfig;
import api.mcnc.surveyrespondentservice.authentication.auth.EmailUserInfo;
import api.mcnc.surveyrespondentservice.client.email.EmailClientService;
import api.mcnc.surveyrespondentservice.client.email.EmailValidateUseCase;
import api.mcnc.surveyrespondentservice.controller.request.EmailVerifyCheckRequest;
import api.mcnc.surveyrespondentservice.controller.request.EmailVerifyRequest;
import api.mcnc.surveyrespondentservice.controller.response.EmailVerifyCheckResponse;
import api.mcnc.surveyrespondentservice.domain.AuthenticatedRespondent;
import api.mcnc.surveyrespondentservice.domain.Token;
import api.mcnc.surveyrespondentservice.service.auth.AuthService;
import api.mcnc.surveyrespondentservice.service.auth.OAuthUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RegisterUseCase;
import api.mcnc.surveyrespondentservice.service.respondent.RespondentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
 * @author :유희준
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

  @MockBean
  EmailClientService emailClientService;

  @Autowired
  OAuthUseCase oauthUseCase;

  @Autowired
  RegisterUseCase registerUseCase;

  @Autowired
  EmailValidateUseCase emailValidateUseCase;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 메일_검증_이메일_송신() throws Exception {
    EmailVerifyRequest request = new EmailVerifyRequest("example@example.com");

    doNothing().when(emailValidateUseCase).requestVerificationCode(request.email());

    mvc.perform(
        post("/auth/email-verify")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andDo(
        document(
          "emailSend",
          requestFields(
            fieldWithPath("email").type(STRING).description("검증할 이메일")
          ),
          responseFields(
            fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지")
          )
        )
      )
      .andDo(print());

  }

  @Test
  void 이메일_검증코드_확인() throws Exception {
    String email = "example@example.com";
    String code = "sdagrl";
    EmailVerifyCheckRequest request = new EmailVerifyCheckRequest(email, code);

    EmailVerifyCheckResponse response = new EmailVerifyCheckResponse(true);
    when(emailValidateUseCase.verifyCode(email, code)).thenReturn(response);

    mvc.perform(
        post("/auth/email-verify/check")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      ).andExpect(status().isOk())
      .andDo(
        document(
          "emailVerifyCheck",
          requestFields(
            fieldWithPath("email").type(STRING).description("인증 이메일"),
            fieldWithPath("code").type(STRING).description("인증 코드")
          ),
          responseFields(
            fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
            fieldWithPath("resultCode").type(STRING).description("응답 코드"),
            fieldWithPath("message").type(STRING).description("응답 메시지"),
            fieldWithPath("body").type(OBJECT).description("응답 데이터"),
            fieldWithPath("body.isValid").type(BOOLEAN).description("검증 성공 여부")
          )
        )
      )
      .andDo(print());
  }

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