package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role extends AbstractBaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;
}
