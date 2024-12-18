package api.mcnc.surveyrespondentservice.authentication.jwt;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-18 오전 11:16
 */
@Builder(access = lombok.AccessLevel.PRIVATE)
public record TokenExtractResponse(
  String respondentId,
  String surveyId
) {
  public static TokenExtractResponse of(String respondentId, String surveyId)  {
    return TokenExtractResponse.builder()
      .surveyId(surveyId)
      .respondentId(respondentId)
      .build();
  }
}
