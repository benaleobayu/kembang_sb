package com.bca.byc.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BusinessHasCategoryId implements Serializable {
    private Long businessId;
    private Long categoryId;
    private Long childCategoryId;

}
