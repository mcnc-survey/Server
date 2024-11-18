package api.mcnc.surveyresponseservice.domain.respondent;

import api.mcnc.surveyresponseservice.entity.answer.AnswerEntity;
import lombok.Builder;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-13 오후 10:21
 */
@Builder
public record Respondent (
  String id,
  String name,
  String email,
  String phoneNumber,
  String provider,
  String surveyId,
  List<AnswerEntity> answers
){ }
