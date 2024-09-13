package com.bca.byc.model.Elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "user_active")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveElastic {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "email")
    private String email;

    @Field(type = FieldType.Boolean, name = "is_active")
    private Boolean isActive;

}