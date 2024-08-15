package com.bca.byc.model.api;


import com.bca.byc.entity.Business;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4110836650683334939L;

    private Long id;
    private String parentName;
    private String name;
    private String email;
    private String phone;
    private String bankAccount;
    private String education;
    private String businessName;
    private String cin;
    private String biodata;
    private String status;
    private String createdAt;
    private String updateAt;



}
