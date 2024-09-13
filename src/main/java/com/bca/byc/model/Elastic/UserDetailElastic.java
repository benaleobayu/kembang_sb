package com.bca.byc.model.Elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@Document(indexName = "app_user_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailElastic {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "member_bank_account")
    private String memberBankAccount;

    @Field(type = FieldType.Date, name = "created_at")
    private LocalDateTime createdAt;


}