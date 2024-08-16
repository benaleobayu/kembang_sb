package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "admins")
public class Admin extends AbstractBaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "remember_token")
    private String rememberToken;

    // relations

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

}
