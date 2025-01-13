package api.mcnc.surveyadminservice.entity.admin;

import org.springframework.security.core.GrantedAuthority;

/**
 * 관리자 role editor 시간 남으면 추가 가능
 *
 * @author :유희준
 * @since :2024-11-18 오후 5:40
 */
public enum AdminRole implements GrantedAuthority {
  ADMIN, EDITOR;


  @Override
  public String getAuthority() {
    return name();
  }
}
