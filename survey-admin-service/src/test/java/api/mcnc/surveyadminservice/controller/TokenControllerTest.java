package api.mcnc.surveyadminservice.controller;

import api.mcnc.surveyadminservice.RestDocsConfig;
import api.mcnc.surveyadminservice.auth.jwt.TokenProvider;
import api.mcnc.surveyadminservice.controller.request.TokenReissueRequest;
import api.mcnc.surveyadminservice.domain.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오전 9:24
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
class TokenControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TokenProvider tokenProvider;

  @Test
  void 토큰_재발급() throws Exception {
    // Given
    String oldAccessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NGI4ZDEwYy1hNmIyLTRmYzYtOWQ2ZC1hODVjZTU0MmNmODIiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MzQyODI2OTEsImV4cCI6MTczNDI4NDQ5MX0.1Jy9bb-XBjJASDo4FEyOrv-CDDDnUz8NJnpwm6w82n2_VU6jl2cXlQdDHRBwYbk9AcqYL_VlVqIjf5hPqL67Pw";
    String oldRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NGI4ZDEwYy1hNmIyLTRmYzYtOWQ2ZC1hODVjZTU0MmNmODIiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MzQyODI2OTEsImV4cCI6MTczNTQ5MjI5MX0.M5XXG6Gd6gdyd3_ZeDdViEaQWFLkONWzkyK728fvS4uj3vw6vWi1hmXWgcVXr4OhvIjEI3CArPf9W1Bd421WNg";
    String newAccessToken = "newAccessToken";
    String newRefreshToken = "newRefreshToken";

    TokenReissueRequest tokenReissueRequest = new TokenReissueRequest(oldAccessToken);

    Token token = new Token("id", newAccessToken, newRefreshToken);
    given(tokenProvider.reissueAccessToken(eq(oldAccessToken), eq(oldRefreshToken))).willReturn(token);

    MockHttpServletRequestBuilder requestBuilder = post("/token/reissue")
      .contentType(APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(tokenReissueRequest))
      .cookie(new Cookie("refreshToken", oldRefreshToken)); // 쿠키에 oldRefreshToken 포함

    // When & Then
    mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.resultCode").value("200"))
      .andExpect(jsonPath("$.message").value("OK"))
      .andExpect(jsonPath("$.body.accessToken").value(newAccessToken))
      .andExpect(header().string("Set-Cookie", containsString("refreshToken=" + newRefreshToken)))
      .andDo(document("token-reissue",
        requestFields(
          fieldWithPath("accessToken").type(STRING).description("기존 accessToken")
        ),
        responseFields(
          fieldWithPath("success").type(BOOLEAN).description("결과 코드"),
          fieldWithPath("resultCode").type(STRING).description("응답 코드"),
          fieldWithPath("message").type(STRING).description("응답 메시지"),
          fieldWithPath("body").type(OBJECT).description("응답 데이터"),
          fieldWithPath("body.accessToken").type(STRING).description("새롭게 발급된 accessToken")
        ),
        responseHeaders(
          headerWithName("Set-Cookie").description("새로운 refreshToken이 포함된 응답 헤더")
        )
      ))
      .andDo(print());
  }


}