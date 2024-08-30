package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "locations")
public class Location extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders")
    private Integer orders;

    @Column(name = "status", columnDefinition = "boolean default false")
    private Boolean status;

    // relations
    @ManyToMany(mappedBy = "locations")
    private Set<Business> businesses = new HashSet<>();

}
