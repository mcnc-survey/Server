package api.mcnc.surveyadminservice.common.audit.interceptor;

import api.mcnc.surveyadminservice.common.audit.authentication.AuthenticationHolder;
import api.mcnc.surveyadminservice.common.audit.authentication.RequestedBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * 요청자 정보 인터셉터
 * @author 유희준
 */
@Component
@RequiredArgsConstructor
public class RequestedByInterceptor implements WebRequestInterceptor {
    public static final String REQUESTED_BY_HEADER = "requested-by";

    private final AuthenticationHolder authenticationHolder;

    @Override
    public void preHandle(WebRequest request) {
        String requestedBy = request.getHeader(REQUESTED_BY_HEADER);

        RequestedBy requestUser = new RequestedBy(requestedBy);

        authenticationHolder.setAuthentication(requestUser);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {
    }
}
