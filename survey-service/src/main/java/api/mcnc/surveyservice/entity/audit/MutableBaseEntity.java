package api.mcnc.surveyservice.entity.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MutableBaseEntity extends BaseEntity {

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    protected LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
}