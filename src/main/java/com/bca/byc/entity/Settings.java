package com.bca.byc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Settings extends AbstractBaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identity", nullable = false, unique = true)
    private String identity;

    @Column(name = "description" , columnDefinition = "text")
    private String description;

    @Column(name = "value")
    private Integer value;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

}
