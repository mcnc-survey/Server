package api.mcnc.surveyadminservice.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인가 실패 시 처리 로직
 * @author :유희준
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        log.error("AccessDeniedException is occurred. ", accessDeniedException);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
    }
}
