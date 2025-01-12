package api.mcnc.surveyadminservice.auth.service;

import api.mcnc.surveyadminservice.common.enums.AdminErrorCode;
import api.mcnc.surveyadminservice.common.enums.AuthErrorCode;
import api.mcnc.surveyadminservice.common.exception.AdminException;
import api.mcnc.surveyadminservice.common.exception.AuthException;
import api.mcnc.surveyadminservice.domain.Admin;
import api.mcnc.surveyadminservice.entity.admin.AdminEntity;
import api.mcnc.surveyadminservice.repository.admin.AdminJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 서비스
 * @author : 유희준
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AdminJpaRepository memberRepository;

    /**
     * 회원 조회
     * @param adminId 회원 아이디
     * @return {@link Admin}회원 정보
     */
    public Admin getMemberOrThrow(String adminId) {
        return memberRepository.findById(adminId)
          .map(AdminEntity::toDomain)
                .orElseThrow(() -> new AdminException(AdminErrorCode.NOT_FOUND));
    }

    /**
     * 회원 유효성 검사
     * @param adminId 회원 아이디
     * @param admin 회원 정보
     */
    public void checkAccess(String adminId, Admin admin) {
        if (!admin.id().equals(adminId)) {
            throw new AuthException(AuthErrorCode.NO_ACCESS);
        }
    }
}
