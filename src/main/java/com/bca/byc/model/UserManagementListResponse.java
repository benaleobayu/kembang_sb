package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementListResponse extends AdminModelBaseDTOResponse<Long> implements Serializable {

    private String branchCode;
    private String name;
    private String birthDate;
    private String email;
    private String memberCin;
    private String phone;
    private String approveAt;
    private String approveBy;
    private String senior;
    private List<String> locations;

}
