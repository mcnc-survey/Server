package api.mcnc.email_service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RequiredArgsConstructor
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
public class EmailServiceTest {

    private final MockMvc mockMvc;

    @Test
    void getBookById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/request-verification-code")
                                .param("email", "n5ybro2@gmail.com")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("인증 코드 발송 완료."))
                .andDo(MockMvcResultHandlers.print());
    }

    // POST 테스트 (다수 이메일 인증 코드 요청)
    @Test
    void requestVerificationCodeForMultipleEmails() throws Exception {
        String emailsJson = "[\"test1@example.com\", \"test2@example.com\", \"test3@example.com\"]";

        mockMvc.perform(MockMvcRequestBuilders.post("/multi-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailsJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("인증 코드 발송 완료."))
                .andDo(MockMvcResultHandlers.print());
    }

    // POST 테스트 (인증 코드 검증 요청)
    @Test
    void verifyCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/verify-code")
                        .param("email", "test@example.com")
                        .param("code", "123456")) // 테스트용 코드
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    // POST 테스트 (HTML 이메일 전송 요청)
    @Test
    void sendHtmlVerificationEmails() throws Exception {
        String requestJson = """
                {   
                  "emails": ["shetoo22@gmail.com", "shetoo22@hs.ac.kr"],
                  "userName": "Sample User",
                  "projectName": "Sample Project",
                  "dynamicLink": "https://example.com/verify"
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/html-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("HTML 이메일 전송 완료."))
                .andDo(MockMvcResultHandlers.print());
    }
}

