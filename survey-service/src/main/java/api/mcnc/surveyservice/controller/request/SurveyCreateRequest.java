package api.mcnc.surveyservice.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-11-19 오후 10:58
 */
public record SurveyCreateRequest(
  @NotBlank
  String title,
  String description,
  @NotNull
  LocalDateTime startAt,
  @Future @NotNull
  LocalDateTime endAt,
  @Valid @NotNull
  List<QuestionCreateRequest> questions
) { }
