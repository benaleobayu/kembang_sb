package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminHasAccountsId implements Serializable {

    private Long adminId;

    private Long accountId;

}
