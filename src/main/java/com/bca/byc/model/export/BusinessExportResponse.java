package com.bca.byc.model.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessExportResponse {

    private Long id;
    private String name;
    private String address;
    private String lineOfBusiness;
    private Boolean isPrimary;
    private String userEmail;
    private String userName;
    private String userCin;

}
