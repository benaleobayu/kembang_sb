package com.bca.byc.entity;

import com.bca.byc.entity.impl.AttrIdentificable;
import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "locations")
public class Location extends AbstractBaseEntityCms implements SecureIdentifiable, AttrIdentificable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "orders")
    private Integer orders;

    // relations
    @OneToMany(mappedBy = "location")
    private Set<BusinessHasLocation> businessHasLocations = new HashSet<>();

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

}
