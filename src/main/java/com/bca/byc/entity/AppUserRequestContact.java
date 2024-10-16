package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_request_contact", indexes = {
        @Index(name = "request_contact_secure_id", columnList = "secure_id")
})
public class AppUserRequestContact extends AbstractBaseEntity implements SecureIdentifiable {

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
}
