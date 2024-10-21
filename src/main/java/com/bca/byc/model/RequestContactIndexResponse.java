package com.bca.byc.model;

import com.bca.byc.enums.RequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestContactIndexResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String email;
    private String phone;
    private RequestType status;
    private String createdAt;
}
