package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
