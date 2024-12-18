package api.mcnc.surveyadminservice.entity.admin;

import api.mcnc.surveyadminservice.auth.dto.OAuth2UserInfo;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.entity.audit.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.KeyGenerator;
import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 5:37
 */
@Entity
@Table(name = "admin")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminEntity extends MutableBaseEntity {
  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "ROLE")
  @Enumerated(EnumType.STRING)
  private AdminRole role;
  @Column(name = "PROVIDER")
  private String provider;

  public static AdminEntity fromDomain(Admin admin) {
    return AdminEntity.builder()
      .id(UUID.randomUUID().toString())
      .name(admin.name())
      .email(admin.email())
      .password(admin.password())
      .role(admin.role())
      .provider(admin.provider())
      .build();
  }

  public static AdminEntity from(OAuth2UserInfo oAuth2UserInfo) {
    return AdminEntity.builder()
      .id(UUID.randomUUID().toString())
      .name(oAuth2UserInfo.getName())
      .email(oAuth2UserInfo.getEmail())
      .role(AdminRole.ADMIN)
      .password(new BCryptPasswordEncoder().encode("{admin}password"))
      .provider(oAuth2UserInfo.getProvider())
      .build();
  }


  public Admin toDomain() {
    return Admin.builder()
      .id(id)
      .name(name)
      .email(email)
      .password(password)
      .role(role)
      .provider(provider)
      .build();
  }

  public AdminEntity changePassword(String newPassword) {
    this.password = newPassword;
    return this;
  }

}
