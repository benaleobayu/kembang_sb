package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
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
@Table(name = "business", indexes = {
    @Index(name = "idx_business_secure_id", columnList = "secure_id", unique = true)
})
public class Business extends AbstractBaseEntity implements SecureIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "province")
    private String province;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
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
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<BusinessHasLocation> businessHasLocations = new HashSet<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<BusinessCatalog> businessCatalogs = new HashSet<>();


    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
     // Optional: You can add a method to directly get the user ID for convenience
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
    
}
