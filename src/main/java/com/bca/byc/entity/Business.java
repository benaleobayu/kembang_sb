package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business")
public class Business extends AbstractBaseEntityNoUUID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "address", length = 80)
    private String address;

    @Column(name = "website", length = 50)
    private String website;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @Column(name = "is_primary", columnDefinition = "boolean default false")
    private Boolean isPrimary = false;

    // relation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private AppUser user;

    @OneToMany(mappedBy = "business", cascade = CascadeType.MERGE, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<BusinessHasCategory> businessCategories = new HashSet<>();

    // make manytomany with location
    @ManyToMany
    @JoinTable(name = "business_has_location",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    @EqualsAndHashCode.Exclude
    private Set<Location> locations = new HashSet<>();


}
