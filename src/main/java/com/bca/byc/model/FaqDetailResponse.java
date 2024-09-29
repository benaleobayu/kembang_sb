package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FaqDetailResponse extends AdminModelBaseDTOResponse {
    private String question;
    private String answer;
    private String description;
    private Integer orders;
    private Boolean status;
}
