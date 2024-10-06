package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BlacklistIndexResponse extends AdminModelBaseDTOResponse {

    private String keyword;

    private Boolean status;

    private Integer orders;

}
