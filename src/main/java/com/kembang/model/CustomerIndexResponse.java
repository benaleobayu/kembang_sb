package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerIndexResponse extends ModelBaseDTOResponse<Long>{

    private String name;
    private String address;
    private String location;
    private String phone;
    private BigDecimal distance;
    private List<String> daySubscribed;
    private Boolean isSubscribed;
}
