package api.mcnc.surveyservice.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-19 오후 10:58
 */
public record SurveyCreateRequest(
  @NotBlank
  String title,
  String description,
  @FutureOrPresent
  LocalDateTime startAt,
  @Future
  LocalDateTime endAt,
  @Valid
  List<QuestionCreateRequest> questions
) { }
