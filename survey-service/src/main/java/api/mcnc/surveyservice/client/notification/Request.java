package api.mcnc.surveyservice.client.notification;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-23 오전 10:07
 */
@Builder
public record Request(
  String surveyId,
  String message,
  Type type
) {
  public static Request of(String surveyId, String message, Type type) {
    return Request.builder()
      .surveyId(surveyId)
      .message(message)
      .type(type)
      .build();
  }
}
