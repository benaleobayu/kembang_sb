package com.bca.byc.model;

import com.bca.byc.enums.RequestType;
import lombok.Data;

@Data
public class RequestContactDetailResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String email ;
    private String phone;
    private RequestType status;
    private String messages;
}
