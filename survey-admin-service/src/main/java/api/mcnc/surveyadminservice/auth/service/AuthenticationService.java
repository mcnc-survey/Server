package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.enums.AuthErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.exception.AuthException;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AdminRepository memberRepository;

    public Admin getMemberOrThrow(String adminId) {
        return memberRepository.findById(adminId)
          .map(AdminEntity::toDomain)
                .orElseThrow(() -> new AdminException(AdminErrorCode.NOT_FOUND));
    }

    public void checkAccess(String adminId, Admin admin) {
        if (!admin.id().equals(adminId)) {
            throw new AuthException(AuthErrorCode.NO_ACCESS);
        }
    }
}
