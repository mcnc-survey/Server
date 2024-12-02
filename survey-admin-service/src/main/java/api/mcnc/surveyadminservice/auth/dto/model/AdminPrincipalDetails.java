package api.mcnc.surveyadminservice.auth.dto.model;

import api.mcnc.surveyadminservice.domain.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-26 오전 11:24
 */
public record AdminPrincipalDetails(
  Admin admin,
  Map<String, Object> attributes,
  String attributeKey
) implements OAuth2User, UserDetails {

  @Override
  public String getName() {
    return attributes.get(attributeKey).toString();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(
      new SimpleGrantedAuthority(admin.role().name()));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return admin.name();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
