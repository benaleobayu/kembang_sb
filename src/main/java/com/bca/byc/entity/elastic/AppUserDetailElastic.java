package com.bca.byc.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "app_user_detail")
public class AppUserDetailElastic extends AbstractBaseElastic{

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "phone", type = FieldType.Text)
    private String phone;

    @Field(name = "avatar", type = FieldType.Text)
    private String avatar;

    @Field(name = "cover", type = FieldType.Text)
    private String cover;

    @Field(name = "biodata", type = FieldType.Text)
    private String biodata;

    @Field(name = "education", type = FieldType.Text)
    private String education;

    @Field(name = "country_code", type = FieldType.Text)
    private String countryCode;

    // -- bank --

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

    // -- bank --

    @Field(name = "type", type = FieldType.Text)
    private String accountType;

    @Field(name = "orders", type = FieldType.Boolean)
    private Boolean isSenior;


    // -- of relations --

    @Field(type = FieldType.Long, name = "app_user_detail_id")
    private Long AppUserDetailId;

    @Field(type = FieldType.Long, name = "app_user_attribute_id")
    private Long AppUserAttributeId;

    @Field(type = FieldType.Long, name = "location_id")
    private Long locationId;

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

    @Override
    public Boolean getIsDeleted() {
        return super.getIsDeleted();
    }
}
