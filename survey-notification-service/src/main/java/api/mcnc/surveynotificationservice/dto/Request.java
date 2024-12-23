package api.mcnc.surveynotificationservice.dto;

import api.mcnc.surveynotificationservice.constants.MsgFormat;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.event.NotificationEvent;

import java.util.Map;

import static api.mcnc.surveynotificationservice.entity.NotificationEntity.Type.*;

public record Request(
        String surveyId,
        String message,
        NotificationEntity.Type type
) {
    public NotificationEvent toEvent(String adminId) {

        Map<NotificationEntity.Type, String> messageMap = Map.of(
                SURVEY_CREATE, MsgFormat.SURVEY_CREATE,
                SURVEY_END, MsgFormat.SURVEY_END,
                SURVEY_DELETE, MsgFormat.SURVEY_DELETE,
                SURVEY_START, MsgFormat.SURVEY_START,
                SURVEY_EDIT, MsgFormat.SURVEY_EDIT
        );

        // 기본 메시지 설정
        String resMessage = messageMap.getOrDefault(type, "에러다 에러");

        // 메시지 포맷 적용
        if (!resMessage.equals("에러다 에러")) {
            resMessage = String.format(resMessage, message);
        }

        return NotificationEvent.builder()
                .adminId(adminId)
                .surveyId(surveyId)
                .message(resMessage)
                .type(type)
                .build();
    }
}
