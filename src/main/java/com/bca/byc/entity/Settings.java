package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "settings", indexes = {
    @Index(name = "idx_settings_secure_id", columnList = "secure_id")
})
public class Settings extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identity", nullable = false, unique = true)
    private String identity;

    @Column(name = "description" , columnDefinition = "text")
    private String description;

    @Column(name = "value")
    private Integer value;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
