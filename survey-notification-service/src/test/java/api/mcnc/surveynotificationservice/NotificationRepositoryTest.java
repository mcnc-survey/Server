//package api.mcnc.surveynotificationservice;
//
//
//import api.mcnc.surveynotificationservice.entity.NotificationEntity;
//import api.mcnc.surveynotificationservice.repository.NotificationRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//public class NotificationRepositoryTest {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Test
//    public void testExistsBySurveyIdAndType() {
//        // Given
//        NotificationEntity notification = NotificationEntity.builder()
//                .id(UUID.randomUUID().toString())
//                .adminId("admin-uuid-1")
//                .surveyId("survey-uuid-1")
//                .type(NotificationEntity.Type.SURVEY_END)
//                .status(NotificationEntity.Status.UNREAD)
//                .createdAt(LocalDateTime.now())
//                .build();
//        notificationRepository.save(notification);
//
//        // When
//        boolean exists = notificationRepository.existsBySurveyIdAndType("survey-uuid-1", NotificationEntity.Type.valueOf("SURVEY_END"));
//
//        // Then
//        assertTrue(exists);
//    }
//}
