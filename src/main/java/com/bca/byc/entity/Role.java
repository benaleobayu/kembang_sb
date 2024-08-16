package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role extends AbstractBaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

}
