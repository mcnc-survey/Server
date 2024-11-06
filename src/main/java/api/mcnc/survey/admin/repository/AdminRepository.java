package api.mcnc.survey.admin.repository;

import api.mcnc.survey.admin.repository.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, String> {
}
