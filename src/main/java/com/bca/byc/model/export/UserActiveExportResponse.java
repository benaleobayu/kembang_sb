package com.bca.byc.model.export;

import com.bca.byc.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserActiveExportResponse {

    private Long id;
    private String branchCode;
    private String name;
    private LocalDate birthdate;
    private String email;
    private String cinNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;

//    public UserActiveExportResponse(AppUser appUser) {
//        this.id = appUser.getId();
//        this.branchCode = appUser.getLocation() != null ? appUser.getLocation().getName() : null;
//        this.name = appUser.getAppUserDetail().getName();
//        this.birthdate =
//                appUser.getAppUserDetail().getUserAs() == null || appUser.getAppUserDetail().getUserAs() == "member"
//                ? appUser.getAppUserDetail().getMemberBirthdate()
//                : appUser.getAppUserDetail().getChildBirthdate();
//        this.email = appUser.getEmail();
//        this.cinNumber =
//                appUser.getAppUserDetail().getUserAs() == null || appUser.getAppUserDetail().getUserAs() == "member"
//                ? appUser.getAppUserDetail().getMemberCin()
//                : appUser.getAppUserDetail().getChildCin();
//        this.phoneNumber = appUser.getAppUserDetail().getPhone() != null ? appUser.getAppUserDetail().getPhone() : null;
//        this.createdAt = appUser.getCreatedAt();
//    }
}
