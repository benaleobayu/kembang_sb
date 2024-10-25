package com.bca.byc.entity.elastic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractBaseElastic {

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    // if needed

    @Field(name = "secure_id", type = FieldType.Text)
    private String secureId;

    @Field(name = "is_active", type = FieldType.Boolean)
    private Boolean isActive = true;

    @Field(name = "is_deleted", type = FieldType.Boolean)
    private Boolean isDeleted = false;

    @Field(name = "created_at", type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second)
    private String createdAt;

    @Field(name = "updated_at", type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second)
    private String updatedAt;

    // if needed

    @Field(name = "created_by", type = FieldType.Long)
    private Long createdBy;

    @Field(name = "updated_by", type = FieldType.Long)
    private Long updatedBy;

}