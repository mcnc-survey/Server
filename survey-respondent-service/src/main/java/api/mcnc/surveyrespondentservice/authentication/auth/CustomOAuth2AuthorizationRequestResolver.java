package api.mcnc.surveyrespondentservice.authentication.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * url에 파라미터 추가하기 위한 resolver
 *
 * @author :유희준
 * @since :2024-12-11 오후 9:03
 */
@RequiredArgsConstructor
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

  private final OAuth2AuthorizationRequestResolver defaultResolver;

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
    return customizeAuthorizationRequest(request, authorizationRequest);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
    return customizeAuthorizationRequest(request, authorizationRequest);
  }

  private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
    if (authorizationRequest == null) {
      return null;
    }

    // URL에서 surveyId 파라미터 추출
    String surveyId = request.getParameter("surveyId");

//    if(StringUtils.hasText(surveyId)) {
    String state = authorizationRequest.getState();

    return OAuth2AuthorizationRequest.from(authorizationRequest)
      .state(state + "&surveyId=" + surveyId)
      .build();
//    }
//    throw new RespondentException(RespondentErrorCode.INVALID_REQUEST, "설문 정보를 포함해야 합니다");
  }
}
