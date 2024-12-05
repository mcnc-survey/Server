package api.mcnc.surveyservice.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-29 오후 4:24
 */
public record SurveyDeleteOrRestoreRequest(
  List<@NotBlank String> surveyIds
) {
}
