//package api.mcnc.surveynotificationservice.repository;
//
//import api.mcnc.surveynotificationservice.entity.SurveyEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface SurveyRepository extends JpaRepository<SurveyEntity, String> {
//
//    // 마감 30분 전까지의 설문을 조회하는 쿼리
//    @Query("SELECT s FROM SurveyEntity s WHERE s.endAt BETWEEN :now AND :thirtyMinutesLater AND s.status = 'ACTIVE'")
//    List<SurveyEntity> findSurveysEndingSoon(@Param("now") LocalDateTime now,
//                                             @Param("thirtyMinutesLater") LocalDateTime thirtyMinutesLater);
//
////    // 마감 완료된 설문을 조회하는 쿼리
////    @Query("SELECT s FROM SurveyEntity s WHERE s.endAt <= :now AND s.status = 'COMPLETED'")
////    List<SurveyEntity> findSurveysCompleted(@Param("now") LocalDateTime now);
//
////    // 100명 달성 설문을 조회하는 쿼리
////    @Query("SELECT s FROM SurveyEntity s WHERE s.responseCount >= 100 AND s.status = 'ACTIVE'")
////    List<SurveyEntity> findSurveysWith100Responses();
//
//
//}
//
