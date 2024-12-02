package api.mcnc.surveyadminservice.filter;

import api.mcnc.surveyadminservice.service.LogAdminHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-10-22 오후 8:24
 */
@Component
@RequiredArgsConstructor
public class UserHistoryLoggingFilter extends OncePerRequestFilter {

  private final LogAdminHistoryService logAdminHistoryService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CompletableFuture.runAsync(() -> log(authentication, request));
    filterChain.doFilter(request, response);
  }

  private void log(Authentication authentication, HttpServletRequest request) {
    String userId = authentication.getName();
    String userRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    String clientIp = request.getRemoteAddr();
    String reqMethod = request.getMethod();
    String reqUrl = request.getRequestURI();
    String reqHeader = getHeaders(request);
    String reqPayload = "payload";

    logAdminHistoryService.log(userId, userRole, clientIp, reqMethod, reqUrl, reqHeader, reqPayload);
  }

  private String getHeaders(HttpServletRequest request) {
    Map<String, String> headersMap = new HashMap<>();

    Enumeration<String> headerNames = request.getHeaderNames();
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        headersMap.put(headerName, headerValue);
      }
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(headersMap);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }


}
