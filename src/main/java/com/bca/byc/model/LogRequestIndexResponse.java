package com.bca.byc.model;

import com.bca.byc.enums.RequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogRequestIndexResponse extends AdminModelBaseDTOResponse {

    private String admin;
    private RequestType to;
    private String note;
    private String createdAt;

    public void setStatus(String to) {
        this.to = RequestType.valueOf(to);
    }
}
