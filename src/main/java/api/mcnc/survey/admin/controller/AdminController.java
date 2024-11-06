package api.mcnc.survey.admin.controller;

import api.mcnc.survey.admin.repository.AdminRepository;
import api.mcnc.survey.admin.repository.entity.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminRepository adminRepository;

    @PostMapping("/admin")
    public ResponseEntity<String> save(@RequestBody AdminSignupRequest request) {
        AdminEntity entity = AdminEntity.toEntity(request);
        adminRepository.save(entity);
        return ResponseEntity.ok("ok");
    }

}
