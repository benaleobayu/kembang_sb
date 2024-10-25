package com.bca.byc.model.Elastic;

import com.bca.byc.model.BranchCodeResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PreRegisterIndexElasticResponse extends AbstractTimestampElastic {

    private String name;
    private String email;
    private String phone;
    private String parentType;
    private String memberType;
    private String description;
    private String memberBankAccount;
    private String parentBankAccount;
    private String memberCin;
    private String parentCin;
    private String memberBirthdate;
    private String parentBirthdate;

    private BranchCodeResponse branchCode;
    private String picName;

//    private String birthDate;

    private Integer orders;
    private String approvalStatus;
}
