package api.mcnc.surveyservice.domain;

import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 1:42
 */
@Builder
public record Question(
  String id,
//  Survey survey, // 순환 참조 때문에 있어야 할 지 고민
  String questionType,
  Integer order,
  String columns,
  String rows
) { }
