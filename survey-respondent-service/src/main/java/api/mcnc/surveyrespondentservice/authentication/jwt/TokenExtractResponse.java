package api.mcnc.surveyrespondentservice.authentication.jwt;

import lombok.Builder;

/**
 * 토큰에서 응답자 아이디, 설문 아이디 추출 결과
 *
 * @author :유희준
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
