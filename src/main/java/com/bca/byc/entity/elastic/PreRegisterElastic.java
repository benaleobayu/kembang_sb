package com.bca.byc.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "pre_register")
public class PreRegisterElastic extends AbstractBaseElastic{

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

    @Field(name = "type_member", type = FieldType.Text)
    private String memberType;

    @Field(name = "type_parent", type = FieldType.Text)
    private String parentType;

    @Field(name = "orders", type = FieldType.Integer)
    private Integer orders;

    @Field(name = "status_approval", type = FieldType.Text)
    private String statusApproval;

    @Field(type = FieldType.Text, name = "branch_id")
    private String branchId;

    @Field(type = FieldType.Text, name = "branch_name")
    private String branchName;

    @Field(type = FieldType.Text, name = "pic_name")
    private String picName;

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
