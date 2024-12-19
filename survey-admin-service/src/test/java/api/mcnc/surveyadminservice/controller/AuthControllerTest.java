package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.RestDocsConfig;
import api.mcnc.surveyadminservice.auth.vault.Vault;
import api.mcnc.surveyadminservice.controller.request.AdminSignInRequest;
import api.mcnc.surveyadminservice.controller.request.AdminSignUpRequest;
import api.mcnc.surveyadminservice.controller.request.EmailDuplicateCheckRequest;
import api.mcnc.surveyadminservice.controller.request.PasswordChangeRequest;
import api.mcnc.surveyadminservice.controller.response.EmailDuplicateCheckResponse;
import api.mcnc.surveyadminservice.domain.Token;
import api.mcnc.surveyadminservice.service.AuthService;
import api.mcnc.surveyadminservice.service.response.AdminSignInResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오전 8:33
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Vault vault;

  @MockBean
  private AuthService authService;

  @Test
  void 이메일_중복검사 () throws Exception {
    String email = "yhj3855@naver.com";
    String encrypt = vault.encrypt(email);

    EmailDuplicateCheckRequest request = new EmailDuplicateCheckRequest();
    request.setEmail(email);

    EmailDuplicateCheckResponse response = EmailDuplicateCheckResponse.isDuplicated(encrypt, true);
    given(authService.checkEmailDuplicate(anyString())).willReturn(response);

    mockMvc.perform(
      post("/auth/email-duplicate-check")
      .contentType(APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request))
    )
      .andExpect(status().isOk())
      .andDo(document("checkEmailDuplicate",
        requestFields(
          fieldWithPath("email").type(STRING).description("중복 검사할 email")
        ),
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지"),
          fieldWithPath("body").type(OBJECT).description("응답 데이터"),
          fieldWithPath("body.email").type(STRING).description("중복 검사한 email"),
          fieldWithPath("body.isDuplicated").type(BOOLEAN).description("중복 여부")
        )

      ))
      .andDo(print())
    ;

  }

  @Test
  void 회원가입() throws Exception {
    String email = "yhj3855@naver.com";
    String encrypt = vault.encrypt(email);

    AdminSignUpRequest request = AdminSignUpRequest.builder()
      .email(email)
      .userName("유희준")
      .phoneNumber("01082003855")
      .password("q1w2e3r4!@")
      .build();

    willDoNothing().given(authService).signUp(any(AdminSignUpRequest.class));

    mockMvc.perform(
        post("/auth/sign-up")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andDo(document("signUp",
        requestFields(
          fieldWithPath("email").type(STRING).description("회원가입 email - 중복 X"),
          fieldWithPath("userName").type(STRING).description("사용자 이름"),
          fieldWithPath("phoneNumber").type(STRING).description("사용자 전화번호 - '-' 미 포함"),
          fieldWithPath("password").type(STRING).description("비밀번호 - 영 소문자, 숫자, 특수문자 포함한 8자리 이상 15자리 이하")
        ),
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지")
        )

      ))
      .andDo(print())
    ;
  }

  @Test
  void 로그인() throws Exception {
    String email = "yhj3855@naver.com";
    String encrypt = vault.encrypt(email);

    AdminSignInRequest request = AdminSignInRequest.builder()
      .email(email)
      .password("q1w2e3r4!@")
      .build();

    Token response = new Token("id","accessToken", "refreshToken");
    AdminSignInResponse tokenResponse = AdminSignInResponse.of("admin name", response);
    given(authService.signIn(any(AdminSignInRequest.class))).willReturn(tokenResponse);

    mockMvc.perform(
        post("/auth/sign-in")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andExpect(header().string("Set-Cookie", containsString("refreshToken=" + response.refreshToken())))
      .andDo(document("signIn",
        requestFields(
          fieldWithPath("email").type(STRING).description("로그인 email"),
          fieldWithPath("password").type(STRING).description("비밀번호 - 영 소문자, 숫자, 특수문자 포함한 8자리 이상 15자리 이하")
        ),
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지"),
          fieldWithPath("body").type(OBJECT).description("응답 데이터"),
          fieldWithPath("body.userName").type(STRING).description("사용자 이름"),
          fieldWithPath("body.accessToken").type(STRING).description("토큰")
        ),
        responseHeaders(
          headerWithName("Set-Cookie").description("refreshToken 쿠키가 포함된 응답 헤더")
        )
      ))
      .andDo(print())
    ;

  }

  @Test
  void 비밀번호_변경_이메일_발신() throws Exception {
    String responseMessage = "이메일 전송을 성공하였습니다.";
    given(authService.sendPasswordChangeEmail(anyString())).willReturn(responseMessage);
    mockMvc.perform(
        get("/auth/password-change")
          .queryParam("email", "example@example.com")
      )
      .andExpect(status().isOk())

      .andDo(document("passwordChangeEmail",
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지"),
          fieldWithPath("body").type(STRING).description("응답 데이터")
        )
      ))
      .andDo(print());
  }
  
  @Test
  void 비밀번호_변경_요청() throws Exception {
    PasswordChangeRequest request = new PasswordChangeRequest("newPassword12!@", "token...");
    doNothing().when(authService).changePassword(any(PasswordChangeRequest.class));
    mockMvc.perform(
        post("/auth/password-change")
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andDo(document("changePassword",
        requestFields(
          fieldWithPath("newPassword").type(STRING).description("변경할 비밀번호"),
          fieldWithPath("token").type(STRING).description("유저 확인을 위한 토큰")
        ),
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지")
        )
      ))
      .andDo(print());
  }

}