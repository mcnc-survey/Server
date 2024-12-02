package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.auth.dto.OAuth2UserInfo;
import api.mcnc.surveyadminservice.auth.dto.model.AdminPrincipalDetails;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final AdminRepository adminRepository;

  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
      .getUserInfoEndpoint().getUserNameAttributeName();

    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
    Admin admin = getOrSave(oAuth2UserInfo);

    return new AdminPrincipalDetails(admin, oAuth2UserAttributes, userNameAttributeName);
  }

  private Admin getOrSave(OAuth2UserInfo oAuth2UserInfo) {
    AdminEntity member = adminRepository.findByEmail(oAuth2UserInfo.email())
      .orElseGet(() -> Admin.from(oAuth2UserInfo));
    return adminRepository.save(member);
  }
}
