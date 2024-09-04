package com.bca.byc.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@Table(name = "permission")
public class Permission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "guard_name", nullable = false)
    private String guardName;

    @Override
    public String getAuthority() {
        return null;
    }
}
