package api.mcnc.surveyadminservice.entity.admin;

import api.mcnc.surveyadminservice.entity.audit.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 5:37
 */
@Entity
@Table(name = "admin")
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
}
