package api.mcnc.surveyrespondentservice.client.oauth;

/**
* please explain class!
* 
* @author       :Uheejoon
* @since        :2024-11-22 오후 1:53
*/

public interface OAuthClientService {
  OAuthToken getToken(String code);
  UserInfo getUserInfo(String accessToken);
  String getProvider();
}
