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

    @ManyToMany
    @JoinTable(name = "accounts_has_channel",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id"))
    private List<Channel> channels = new ArrayList<>();

}