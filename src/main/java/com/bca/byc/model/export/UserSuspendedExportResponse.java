package com.bca.byc.model.export;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserSuspendedExportResponse {

    private Long id;
    private String branchCode;
    private String name;
    private LocalDate birthdate;
    private String email;
    private String cinNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;

}
