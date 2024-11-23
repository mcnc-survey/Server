package api.mcnc.surveyrespondentservice.domain;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-20 오후 10:22
 */
@Builder
public record Respondent(
  String id,
  String name,
  String email,
  String phoneNumber,
  String provider,
  String surveyId
) {
  public static Respondent of(AuthenticatedUser user, String surveyId) {
    return Respondent.builder()
      .name(user.name())
      .email(user.email())
      .phoneNumber(user.phoneNumber())
      .provider(user.provider())
      .surveyId(surveyId)
      .build();
  }
}
