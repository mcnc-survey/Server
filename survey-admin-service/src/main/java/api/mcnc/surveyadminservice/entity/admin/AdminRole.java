package api.mcnc.surveyadminservice.entity.admin;

import org.springframework.security.core.GrantedAuthority;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 5:40
 */
public enum AdminRole implements GrantedAuthority {
  ADMIN, EDITOR;


  @Override
  public String getAuthority() {
    return name();
  }
}
