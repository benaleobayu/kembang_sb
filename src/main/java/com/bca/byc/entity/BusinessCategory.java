package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_categories", indexes = {
        @Index(name = "idx_business_category_secure_id", columnList = "secure_id", unique = true)
})
public class BusinessCategory extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    // make parent in this entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BusinessCategory parentId;

    // show data as child in this entity
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<BusinessCategory> children = new ArrayList<>();

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getActive() {
        return super.isActive();
    }

}
