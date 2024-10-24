package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "accounts", indexes = {@Index(name = "uk_accounts_secure_id", columnList = "secure_id", unique = true)})
public class Account extends AbstractBaseEntityCms implements SecureIdentifiable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "cover", columnDefinition = "text")
    private String cover;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountHasChannels> accountHasChannels = new ArrayList<>();

}