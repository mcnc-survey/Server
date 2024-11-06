package api.mcnc.survey.admin.repository.entity;

import api.mcnc.survey.admin.controller.AdminSignupRequest;
import api.mcnc.survey.common.audit.entity.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "admin")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminEntity extends MutableBaseEntity {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    public static AdminEntity toEntity(AdminSignupRequest adminSignupRequest) {
        return AdminEntity.builder()
                .id(UUID.randomUUID().toString())
                .email(adminSignupRequest.email())
                .password(adminSignupRequest.password())
                .role(adminSignupRequest.role())
                .build();
    }
}
