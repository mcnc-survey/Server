package api.mcnc.surveyadminservice.service;

import api.mcnc.surveyadminservice.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 관리자 검증 서비스
 *
 * @author :유희준
 * @since :2025-01-09 오후 1:39
 */
@Service
@RequiredArgsConstructor
public class AdminValidService {

  private final AdminRepository adminRepository;

  public boolean isExistById(String id) {
    return adminRepository.isExistById(id);
  }
}
