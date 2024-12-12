package com.kembang.entity;

import com.kembang.entity.impl.AttrIdentificable;
import com.kembang.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "locations")
public class Location extends AbstractBaseEntity implements SecureIdentifiable, AttrIdentificable {

    @Override
    public Long getId() {return super.getId();}

    @Override
    public String getSecureId() {return super.getSecureId();}

    @Override
    public Boolean getIsActive() {return super.getIsActive();}

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "orders")
    private Integer orders;

    // relations

}
