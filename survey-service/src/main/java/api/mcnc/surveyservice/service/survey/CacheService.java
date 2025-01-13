package api.mcnc.surveyservice.service.survey;

import api.mcnc.surveyservice.client.admin.AdminServiceClientService;
import api.mcnc.surveyservice.common.audit.authentication.RequestedByProvider;
import api.mcnc.surveyservice.common.exception.custom.SurveyException;
import api.mcnc.surveyservice.domain.Survey;
import api.mcnc.surveyservice.repository.survey.FetchSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static api.mcnc.surveyservice.common.enums.SurveyErrorCode.FOUND_NOT_SURVEY;
import static api.mcnc.surveyservice.common.enums.SurveyErrorCode.INVALID_REQUEST;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2025-01-09 오후 2:45
 */
@Service
@RequiredArgsConstructor
public class CacheService {
  private final AdminServiceClientService adminServiceClientService;
  private final FetchSurveyRepository fetchSurveyRepository;
  private final RequestedByProvider provider;

  @Cacheable(cacheNames = "survey", key = "'survey:id:' + #surveyId", cacheManager = "surveyCacheManager")
  public Survey getSurvey(String surveyId) {
    String adminId = getAdminId();
    return fetchSurveyRepository.fetchBySurveyIdAndAdminId(surveyId, adminId)
      .orElseThrow(() -> new SurveyException(FOUND_NOT_SURVEY, "설문이 존재하지 않습니다."));
  }

  // 작성자 아이디 가져오기
  public String getAdminId() {
    String adminId = provider.requestedBy().orElse("SYSTEM");
    boolean isExistAdmin = adminServiceClientService.isExistAdmin(adminId);
    if (!isExistAdmin) {
      throw new SurveyException(INVALID_REQUEST, "관리자 아이디가 일치하지 않습니다.");
    }
    return adminId;
  }

}
