package api.mcnc.surveyadminservice.oauth2;

import api.mcnc.surveyadminservice.RestDocsConfig;
import api.mcnc.surveyadminservice.auth.vault.Vault;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-16 오후 12:49
 */
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@SpringBootTest
public class OAuth2Test {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void OAuth2_테스트() throws Exception {
    mockMvc.perform(get("/oauth2/authorization/{social}", "kakao"))
      .andExpect(status().is3xxRedirection())
      .andDo(document("oauth2",
        pathParameters(
          parameterWithName("social").description("소셜 로그인 타입 - kakao, google, naver")
        )
      ));
  }
}
