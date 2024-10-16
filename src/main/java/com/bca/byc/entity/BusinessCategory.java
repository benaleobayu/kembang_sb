package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.entity.impl.AttrIdentificable;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
        @Index(name = "business_category_secure_id", columnList = "secure_id", unique = true)
})
public class BusinessCategory extends AbstractBaseEntityCms implements SecureIdentifiable, AttrIdentificable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Column(name = "is_parent", columnDefinition = "boolean default false")
    private Boolean isParent = false;

    // make parent in this entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BusinessCategory parentId;

    // show data as child in this entity
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BusinessCategory> children = new ArrayList<>();

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }


    @Override
    public String getName() {
        return name; // or another relevant field
    }

}
