package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ReasonReport extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "icon", columnDefinition = "text")
    private String icon;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}