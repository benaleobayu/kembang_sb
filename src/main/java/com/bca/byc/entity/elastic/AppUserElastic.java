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
@Document(indexName = "app_user")
public class AppUserElastic extends AbstractBaseElastic{

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "email", type = FieldType.Text)
    private String email;

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
}
