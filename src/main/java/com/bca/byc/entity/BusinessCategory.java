package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_categories")
public class BusinessCategory extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    // make parent in this entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BusinessCategory parentId;

    // show data as child in this entity
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<BusinessCategory> children = new ArrayList<>();

}
