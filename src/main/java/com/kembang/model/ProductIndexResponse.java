package com.kembang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductIndexResponse extends ModelBaseDTOResponse<Long>{

    private String name;

    private String code;

    private String description;

    private String image;

    private String category;

    private Integer price;

    private Boolean isActive;

}
