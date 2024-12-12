package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerIndexResponse extends ModelBaseDTOResponse<Long>{

    private String name;
    private String address;
    private String location;
    private String phone;
    private Integer distance = 0;
    private List<String> daySubscribed;
    private Boolean isSubscribed;
}
