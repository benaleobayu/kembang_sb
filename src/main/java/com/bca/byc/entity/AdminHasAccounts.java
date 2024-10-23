package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "admin_has_accounts")
public class AdminHasAccounts {

    @EmbeddedId
    private AdminHasAccountsId id = new AdminHasAccountsId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("adminId")
    @JoinColumn(name = "admin_id")
    private AppAdmin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

}
