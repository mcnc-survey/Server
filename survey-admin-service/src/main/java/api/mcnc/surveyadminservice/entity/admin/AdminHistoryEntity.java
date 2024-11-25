package api.mcnc.surveyadminservice.entity.admin;

import api.mcnc.surveyadminservice.domain.AdminHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-18 오후 5:40
 */
@Entity
@Table(name = "ADMIN_HISTORIES")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminHistoryEntity {
  @Id
  @Column(name = "ID", nullable = false)
  private String id;
  @Column(name = "ADMIN_ID")
  private String adminId;
  @Column(name = "ADMIN_ROLE")
  private String adminRole;
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

  public static AdminHistoryEntity from(AdminHistory adminHistory) {
    return AdminHistoryEntity.builder()
      .id(UUID.randomUUID().toString())
      .adminId(adminHistory.adminId())
      .adminRole(adminHistory.adminRole())
      .clientIp(adminHistory.clientIp())
      .reqMethod(adminHistory.reqMethod())
      .reqUrl(adminHistory.reqUrl())
      .reqHeader(adminHistory.reqHeader())
      .reqPayload(adminHistory.reqPayload())
      .build();
  }
}
