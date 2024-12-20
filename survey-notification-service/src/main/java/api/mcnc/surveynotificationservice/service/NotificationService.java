//package api.mcnc.surveynotificationservice.service;
//import api.mcnc.surveynotificationservice.dto.Request;
//import api.mcnc.surveynotificationservice.entity.NotificationEntity;
//import api.mcnc.surveynotificationservice.repository.NotificationRepository;
//import lombok.Builder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@Builder
//@Service
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository) {
//        this.notificationRepository = notificationRepository;
//    }
//
//    // 미확인된 알림의 개수 조회
//    public long countUnreadNotifications(String adminId) {
//        return notificationRepository.countByAdminIdAndStatus(adminId, NotificationEntity.Status.UNREAD);
//    }
//
//
//    // 알림 상태를 무조건 READ로 업데이트
//    public void updateNotificationStatus(String notificationId) {
//        notificationRepository.updateStatusToReadById(notificationId);
//    }
//
//
//    // 모든 알림 조회
//    public List<NotificationEntity> getAllNotificationsByAdmin(String adminId) {
//        return notificationRepository.findByAdminId(adminId);
//    }
//
//    // 알림 삭제
//    public void deleteNotificationById(String id) {
//        notificationRepository.deleteById(id);
//    }
//
//
//    // 알림 여러 개
//    public void createNotifications(List<Request> requests) {
//        for (Request request : requests) {
//            boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
//            if (!exists) {
//                NotificationEntity notification = NotificationEntity.builder()
//                        .id(UUID.randomUUID().toString())
//                        .surveyId(request.surveyId())
//                        .adminId(request.adminId())
//                        .type(request.type())
//                        .status(NotificationEntity.Status.UNREAD)
//                        .createdAt(LocalDateTime.now())
//                        .build();
//                notificationRepository.save(notification);
//            }
//        }
//    }
//
//
//    // 알림 한 개
//    public void createNotification(Request request) {
//        boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
//        if (!exists) {
//            NotificationEntity notification = NotificationEntity.builder()
//                    .id(UUID.randomUUID().toString())
//                    .surveyId(request.surveyId())
//                    .adminId(request.adminId())
//                    .type(request.type())
//                    .status(NotificationEntity.Status.UNREAD)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            notificationRepository.save(notification);
//        }
//    }
//
//}
//
package api.mcnc.surveynotificationservice.service;

import api.mcnc.surveynotificationservice.constants.MsgFormat;
import api.mcnc.surveynotificationservice.dto.Request;
import api.mcnc.surveynotificationservice.entity.NotificationEntity;
import api.mcnc.surveynotificationservice.repository.NotificationRepository;
import api.mcnc.surveynotificationservice.repository.SseEmitterRepository;
import api.mcnc.surveynotificationservice.dto.NotificationDto;
import api.mcnc.surveynotificationservice.exception.NotificationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final RedisMessageService redisMessageService;
    private final SseEmitterService sseEmitterService;
    private final ObjectMapper objectMapper;

    /**
     * 미확인된 알림의 개수 조회
     *
     * @param adminId 관리자의 ID
     * @return 미확인 알림의 개수
     */
    public long countUnreadNotifications(String adminId) {
        return notificationRepository.countByAdminIdAndStatus(adminId, NotificationEntity.Status.UNREAD);
    }

    /**
     * 알림 상태를 'READ'로 업데이트
     *
     * @param id 알림 ID
     */
    public void updateNotificationStatus(String id) {
        notificationRepository.updateStatusToReadById(id);
    }

    /**
     * 관리자의 모든 알림 조회
     *
     * @param adminId 관리자의 ID
     * @return 알림 목록
     */
    public List<NotificationEntity> getAllNotificationsByAdmin(String adminId) {
        return notificationRepository.findByAdminId(adminId);
    }

    /**
     * 알림 삭제
     *
     * @param id 알림 ID
     */
    public void deleteNotificationById(String id) {
        notificationRepository.deleteById(id);
    }

//    /**
//     * 여러 개 알림 생성
//     *
//     * @param requests 알림 요청 리스트
//     */
//    public void createNotifications(List<Request> requests) {
//        for (Request request : requests) {
//            boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
//            if (!exists) {
//                // id를 UUID로 생성하여 명시적으로 넣어줍니다.
//                NotificationEntity notification = NotificationEntity.builder()
//                        .surveyId(request.surveyId())
//                        .adminId(request.adminId())
//                        .type(request.type())
//                        .status(NotificationEntity.Status.UNREAD)
//                        .createdAt(LocalDateTime.now())
//                        .build();
//
//                notificationRepository.save(notification);
//
//                // SSE로 알림 전송
//                sendSseNotification(request.adminId(), notification);
//            }
//        }
//    }

    /**
     * 여러 개 알림 생성
     *
     * @param base64EncodedInfo 알림 요청 리스트
     */
    public void createNotifications(String base64EncodedInfo) throws IOException {
        // Base64 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedInfo);
        String decodedString = new String(decodedBytes);

        // JSON 데이터를 Request 객체 목록으로 변환
        List<Request> requests = objectMapper.readValue(decodedString, new TypeReference<List<Request>>() {});

        for (Request request : requests) {
            boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
            if (!exists) {
                // id를 UUID로 생성하여 명시적으로 넣어줍니다.
                NotificationEntity notification = NotificationEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .surveyId(request.surveyId())
                        .adminId(request.adminId())
                        .type(request.type())
                        .status(NotificationEntity.Status.UNREAD)
                        .createdAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                // SSE로 알림 전송
                sendSseNotification(request.adminId(), notification);
            }
        }
    }

    /**
     * 한 개 알림 생성
     *
     * @param request 알림 요청
     */
    public void createNotification(Request request) {
        boolean exists = notificationRepository.existsBySurveyIdAndType(request.surveyId(), request.type());
        if (!exists) {
            NotificationEntity notification = NotificationEntity.builder()
                    .surveyId(request.surveyId())
                    .adminId(request.adminId())
                    .type(request.type())
                    .status(NotificationEntity.Status.UNREAD)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);

            // SSE로 알림 전송
            sendSseNotification(request.adminId(), notification);
        }
    }

    /**
     * SSE를 통해 실시간 알림 전송
     *
     * @param adminId    관리자의 ID
     * @param notification 알림 객체
     */
    private void sendSseNotification(String adminId, NotificationEntity notification) {
        SseEmitter sseEmitter = sseEmitterRepository.findById(adminId)
                .orElseThrow(() -> new NotificationException("SSE Emitter not found for adminId: " + adminId));

        NotificationDto notificationDto = NotificationDto.fromEntity(notification);

        try {
            sseEmitter.send(SseEmitter.event()
                    .data(notificationDto));
        } catch (Exception e) {
            // 오류 발생 시 SSE 연결 종료 및 처리
            sseEmitterRepository.deleteById(adminId);
            throw new NotificationException("Failed to send notification via SSE");
        }

        // Redis로 알림 전달
        redisMessageService.publish(adminId, notificationDto);
    }

    public SseEmitter subscribe(String adminId) {
        SseEmitter sseEmitter = sseEmitterService.createEmitter(adminId);
        sseEmitterService.send(MsgFormat.SUBSCRIBE, adminId, sseEmitter); // send dummy

        redisMessageService.subscribe(adminId);

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError((e) -> sseEmitter.complete());
        sseEmitter.onCompletion(() -> {
            sseEmitterService.deleteEmitter(adminId);
            redisMessageService.removeSubscribe(adminId);
        });
        return sseEmitter;
    }
}
