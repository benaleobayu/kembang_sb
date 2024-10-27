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
@Document(indexName = "branch")
public class BranchElastic extends AbstractBaseElastic{

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "address", type = FieldType.Text)
    private String address;

    @Field(name = "code", type = FieldType.Text)
    private String code;

    @Field(name = "phone", type = FieldType.Text)
    private String phone;

    @Field(name = "parent_cin", type = FieldType.Text)
    private String parentCin;

    // -- of relations --
    @Field(type = FieldType.Long, name = "kanwil_id")
    private Long kanwilId;

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
}
