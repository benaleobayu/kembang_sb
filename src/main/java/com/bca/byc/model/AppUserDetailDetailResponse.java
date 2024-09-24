package com.bca.byc.model;

import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppUserDetailDetailResponse {

    private String name;

    private String phone;

    private String memberBankAccount;

    private String parentBankAccount;

    private LocalDate memberBirthdate;

    private LocalDate parentBirthdate;

    private String memberCin;

    private String parentCin;

    private String education;

    private String biodata;

    private StatusType status;

    private UserType type;

    private String memberType;

    private String ApprovedBy;

    private LocalDateTime ApprovedAt;

    private String avatar;

    private String cover;

}
