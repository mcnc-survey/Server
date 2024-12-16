package api.mcnc.surveynotificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.print.DocFlavor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateNotification() throws Exception {
        // Base64 인코딩된 요청 데이터
        String base64Request = "eyJhZG1pbklkIjoiYWRtaW4tdXVpZC0xIiwic3VydmV5SWQiOiJzdXJ2ZXktdXVpZC0xIiwidHlwZSI6IlNVUlZFWV9FTkQifQ==";

        // MockMvc를 사용해 POST 요청 보내기
        ResultActions result = mockMvc.perform(post("/notifications/create")
                .content(base64Request)
                .contentType(MediaType.TEXT_PLAIN));

        result.andDo(
                MockMvcRestDocumentation.document("알림 생성",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("info").type(JsonFieldType.STRING).description("알림 정보")
                        )
                )
        );

        // 결과 검증
        result.andExpect(status().isOk())  // 상태 코드 200 OK 확인
                .andExpect(content().string("Notification created successfully."))
                .andDo(MockMvcResultHandlers.print()); // 응답 본문 검증
    }

    @Test
    public void testCreateBulkNotifications() throws Exception {
        // Base64 인코딩된 요청 데이터
        String base64Request = "W3siYWRtaW5JZCI6ImFkbWluLXV1aWQtMSIsInN1cnZleUlkIjoic3VydmV5LXV1aWQtMSIsInR5cGUiOiJTVVJWRVlfRU5EIn0seyJhZG1pbklkIjoiYWRtaW4tdXVpZC0yIiwic3VydmV5SWQiOiJzdXJ2ZXktdXVpZC0yIiwidHlwZSI6IlNVUlZFWV9FTkRJTkdfU09PTiJ9XQ==";

        // MockMvc를 사용해 POST 요청 보내기
        ResultActions result = mockMvc.perform(post("/notifications/create-bulk")
                .content(base64Request)
                .contentType(MediaType.TEXT_PLAIN));

        // 결과 검증
        result.andExpect(status().isOk())  // 상태 코드 200 OK 확인
                .andExpect(content().string("Notifications created successfully for all requests.")); // 응답 본문 검증
    }


    @Test
    public void testCountUnreadNotifications() throws Exception {
        String adminId = "admin-uuid-1";

        mockMvc.perform(get("/notifications/count-unread")
                        .param("adminId", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    public void testUpdateNotificationStatus() throws Exception {
        String notificationId = "9751855b-07c2-482f-a83c-be2496d9bb60";

        mockMvc.perform(patch("/notifications/{id}/status", notificationId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllNotificationsByAdmin() throws Exception {
        String adminId = "admin-uuid-1";

        mockMvc.perform(get("/notifications/findAll")
                        .param("adminId", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testDeleteNotification() throws Exception {
        String notificationId = "9751855b-07c2-482f-a83c-be2496d9bb60";

        mockMvc.perform(delete("/notifications/{id}/delete", notificationId))
                .andExpect(status().isNoContent());
    }

}
