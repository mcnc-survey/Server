package api.mcnc.surveyservice.controller.request;

import jakarta.validation.constraints.Email;

import java.util.List;

/**
 * please explain class!
 *
 * @author :유희준
 * @since :2024-12-18 오후 4:18
 */
public record SurveyInviteRequest(
  List<@Email String> emails
) {
}
