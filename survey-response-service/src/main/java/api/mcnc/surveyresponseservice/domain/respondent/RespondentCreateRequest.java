package api.mcnc.surveyresponseservice.domain.respondent;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 10:27
 */
@Builder
public record RespondentCreateRequest(
  String name,
  String email,
  String phoneNumber,
  String provider,
  String surveyId
) { }
