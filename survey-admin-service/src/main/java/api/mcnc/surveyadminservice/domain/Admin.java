package api.mcnc.surveyadminservice.domain;

import api.mcnc.surveyadminservice.entity.admin.AdminRole;
import lombok.Builder;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-11-25 오후 10:22
 */
@Builder
public record Admin(
  String id,
  String name,
  String email,
  String password,
  AdminRole role,
  String provider
) {
}
