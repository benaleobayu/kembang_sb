package com.bca.byc.model;

import com.bca.byc.entity.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleUserDetailResponse {

    private String name;
    private String email;
    private StatusType status;

}
