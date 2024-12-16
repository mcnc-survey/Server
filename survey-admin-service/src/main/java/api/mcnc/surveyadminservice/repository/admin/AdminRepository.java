package api.mcnc.surveyadminservice.repository.admin;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static api.mcnc.surveyadminservice.common.constants.Provider.EMAIL;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-12 오후 1:47
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class AdminRepository {

  private final AdminJpaRepository adminJpaRepository;

  public Optional<Admin> getByEmailAndProvider(String email, String provider) {
    return adminJpaRepository.findByEmailAndProvider(email, provider)
      .map(AdminEntity::toDomain);
  }

  public Admin registerWithSocial(Admin admin) {
    return getByEmailAndProvider(admin.email(), admin.provider())
      .orElseGet(() -> adminJpaRepository.save(AdminEntity.fromDomain(admin)).toDomain());
  }

  public Admin registerWithEmail(Admin admin) {
    AdminEntity adminEntity = AdminEntity.fromDomain(admin);
    AdminEntity saveEntity = adminJpaRepository.save(adminEntity);
    return saveEntity.toDomain();
  }

  public Optional<Admin> getByEmailAdmin(String email) {
    return this.getByEmailAndProvider(email, EMAIL);
  }
}
