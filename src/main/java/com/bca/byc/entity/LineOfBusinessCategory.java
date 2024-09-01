package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "line_of_business_category")
public class LineOfBusinessCategory extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    @ManyToMany
    @JoinTable(name = "lob_has_category",
            joinColumns = @JoinColumn(name = "line_of_business_category_id"),
            inverseJoinColumns = @JoinColumn(name = "line_of_business_item_id"))
    private Set<LineOfBusinessItem> lobItem = new HashSet<>();

}