package api.mcnc.surveyservice.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-23 오전 11:00
 */
public record SurveyRestoreRequest(
  List<@NotBlank String> surveyIdList
) {
}
