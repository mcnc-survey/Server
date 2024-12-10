package api.mcnc.surveyresponseservice.client.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-10 오전 10:31
 */
@Slf4j
@Component
public class SruveyResponseErrorDecoder implements ErrorDecoder {
  @Override
  public Exception decode(String s, Response response) {
    // 상태 코드별 처리
    return switch (response.status()) {
      case 400 -> {
        log.error("Bad Request: {}", response.reason());
        yield new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
      }
      case 404 -> {
        log.error("Not Found: {}", response.reason());
        yield new ResponseStatusException(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다.");
      }
      case 500 -> {
        log.error("Internal Server Error: {}", response.reason());
        yield new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");
      }
      default -> {
        log.error("Unexpected Error: {} - {}", response.status(), response.reason());
        yield new Exception("알 수 없는 에러가 발생했습니다.");
      }
    };
  }
}
