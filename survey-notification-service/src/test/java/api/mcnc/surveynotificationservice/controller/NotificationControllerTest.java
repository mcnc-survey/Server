package api.mcnc.surveynotificationservice.controller;

import api.mcnc.surveynotificationservice.RestDocsConfig;
import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.event.NotificationEventPublisher;
import api.mcnc.surveynotificationservice.service.NotificationService;
import api.mcnc.surveynotificationservice.service.RedisMessageService;
import api.mcnc.surveynotificationservice.service.SseEmitterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@WebMvcTest(NotificationController.class) // 테스트할 Controller만 로드
@DisplayName("알림 테스트")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;
    @MockBean
    private SseEmitterService sseEmitterService;
    @MockBean
    private RedisMessageService redisMessageService;
    @MockBean
    private NotificationEventPublisher publisher;

    @Autowired
    private ObjectMapper objectMapper;

    String adminId = "7a762f88-0aa9-48b0-8a12-fc680523cd67";
    Long notificationId = 24L;

    @Test
    void markNotificationAsRead() throws Exception {
        mockMvc.perform(
                        patch("/notifications/read/{id}", notificationId)
                                .header("requested-by", adminId)
                )
                .andExpect(status().is3xxRedirection())
                .andDo(
                        document(
                                "unReadToRead",
                                requestHeaders(
                                        headerWithName("requested-by").description("관리자 아이디")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("알림 아이디")
                                )
                        )
                )
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("전체 조회(30개)")
    void getAllNotifications() throws Exception {
        List<NotificationDto> response = List.of(
                NotificationDto.of(1L, "[설문 1]이 생성되었습니다.", false, LocalDateTime.now().toString(), NotificationEntity.Type.SURVEY_CREATE),
                NotificationDto.of(2L, "[설문 2]이 시작되었습니다.", false, LocalDateTime.now().toString(), NotificationEntity.Type.SURVEY_START),
                NotificationDto.of(3L, "[설문 3]이 종료되었습니다.", false, LocalDateTime.now().toString(), NotificationEntity.Type.SURVEY_END),
                NotificationDto.of(4L, "[설문 4]이 수정되었습니다.", false, LocalDateTime.now().toString(), NotificationEntity.Type.SURVEY_EDIT),
                NotificationDto.of(5L, "[설문 5]이 삭제되었습니다.", false, LocalDateTime.now().toString(), NotificationEntity.Type.SURVEY_DELETE)
        );
        BDDMockito.given(notificationService.getAllNotificationsByAdmin(adminId)).willReturn(response);

        mockMvc.perform(
                        get("/notifications", notificationId)
                                .header("requested-by", adminId)
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "getAllNotifications",
                                requestHeaders(
                                        headerWithName("requested-by").description("관리자 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("알림 응답"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("알림 아이디"),
                                        fieldWithPath("[].message").type(JsonFieldType.STRING).description("알림 메시지"),
                                        fieldWithPath("[].isRead").type(JsonFieldType.BOOLEAN).description("읽음 여부"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                        fieldWithPath("[].type").type(JsonFieldType.STRING).description("알림 종류")
                                )
                        )
                )
                .andDo(print())
        ;

    }

    @Test
    void deleteNotification() throws Exception{
        mockMvc.perform(
                        delete("/notifications/delete/{id}", notificationId)
                                .header("requested-by", adminId)
                )
                .andExpect(status().isNoContent())
                .andDo(
                        document(
                                "deleteNotification",
                                requestHeaders(
                                        headerWithName("requested-by").description("관리자 아이디")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("알림 아이디")
                                )
                        )
                )
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("SSE 구독 요청 테스트")
    void subscribe() throws Exception{
        mockMvc.perform(
                        get("/notifications/subscribe")
                                .header("requested-by", adminId)
                                .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
                )
                .andExpect(status().isOk()) // 상태 코드가 200인지 확인
                .andExpect(header().string("Content-Type", MediaType.TEXT_EVENT_STREAM_VALUE)) // Content-Type 확인
                .andDo(
                        document(
                                "subscribe",
                                requestHeaders(
                                        headerWithName("requested-by").description("관리자 아이디")
                                ),
                                responseHeaders(
                                        headerWithName("Content-Type").description("SSE 스트림 데이터의 MIME 타입")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void unsubscribeFromNotifications() throws Exception {

        mockMvc.perform(
                        delete("/notifications/unsubscribe")
                                .header("requested-by", adminId)
                )
                .andExpect(status().isNoContent())
                .andDo(
                        document(
                                "unsubscribe",
                                requestHeaders(
                                        headerWithName("requested-by").description("관리자 아이디")
                                )
                        )
                )
                .andDo(print());
    }
}