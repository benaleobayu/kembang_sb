package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.enums.RequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_request_contact", indexes = {
        @Index(name = "request_contact_secure_id", columnList = "secure_id")
})
public class AppUserRequestContact extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;

    @Column(name = "messages", columnDefinition = "TEXT")
    private String messages;

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private RequestType status = RequestType.PENDING;

    @ManyToOne
    @JoinColumn(name = "app_user_id", insertable = false, updatable = false)
    private AppUser user;
}
