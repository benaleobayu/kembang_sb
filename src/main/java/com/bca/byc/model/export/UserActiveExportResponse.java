package com.bca.byc.model.export;

import com.bca.byc.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveExportResponse {

    private String branch;
    private String name;
    private LocalDate birthdate;
    private String email;
    private String cinNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;

}
