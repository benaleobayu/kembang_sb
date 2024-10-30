package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BroadcastIndexResponse extends AdminModelBaseDTOResponse<Long> {

    private String title;

    private String message;

    private String status;

    private String postAt;

    private Boolean isSent = false;

}
