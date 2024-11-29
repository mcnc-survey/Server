package api.mcnc.surveyrespondentservice.service.respondent;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오후 9:41
 */
public interface ValidateUseCase {
  boolean validateRespondent(String respondentId);
  String extractSubject(String token);
}
