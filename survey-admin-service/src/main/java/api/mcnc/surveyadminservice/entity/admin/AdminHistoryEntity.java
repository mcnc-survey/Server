package api.mcnc.surveyadminservice.entity.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 5:40
 */
@Entity
@Table(name = "ADMIN_HISTORIES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminHistoryEntity {
  @Id
  @Column(name = "ID", nullable = false)
  private String id;
  @Column(name = "ADMIN_ID")
  private String adminId;
  @Column(name = "ADMIN_ROLE")
  private AdminRole adminRole;
  @Column(name = "REQ_IP")
  private String clientIp;
  @Column(name = "REQ_METHOD")
  private String reqMethod;
  @Column(name = "REQ_URL")
  private String reqUrl;
  @Column(name = "REQ_HEADER")
  private String reqHeader;
  @Column(name = "REQ_PAYLOAD")
  private String reqPayload;
}
