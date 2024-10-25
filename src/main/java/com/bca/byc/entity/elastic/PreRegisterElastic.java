package com.bca.byc.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "pre_register")
public class PreRegisterElastic {

    @Id
    @Field(name = "secure_id", type = FieldType.Text)
    private String secureId;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "email", type = FieldType.Text)
    private String email;

    @Field(name = "phone", type = FieldType.Text)
    private String phone;

    @Field(name = "parent_bank_account", type = FieldType.Text)
    private String parentBankAccount;

    @Field(name = "parent_cin", type = FieldType.Text)
    private String parentCin;

    @Field(name = "parent_birthdate", type = FieldType.Date)
    private LocalDate parentBirthdate;

    @Field(name = "member_bank_account", type = FieldType.Text)
    private String memberBankAccount;

    @Field(name = "member_cin", type = FieldType.Text)
    private String memberCin;

    @Field(name = "member_birthdate", type = FieldType.Date)
    private LocalDate memberBirthdate;

    @Field(name = "member_type", type = FieldType.Text)
    private String memberType;

    @Field(name = "parent_type", type = FieldType.Text)
    private String parentType;

    @Field(name = "orders", type = FieldType.Integer)
    private Integer orders;

    @Field(name = "approval_status", type = FieldType.Text)
    private String statusApproval;

    @Field(name = "is_active", type = FieldType.Boolean)
    private Boolean isActive = false;

    @Field(type = FieldType.Text, name = "branch_id")
    private String branchId;

    @Field(type = FieldType.Text, name = "branch_name")
    private String branchName;

    @Field(type = FieldType.Text, name = "pic_name")
    private String picName;

}
