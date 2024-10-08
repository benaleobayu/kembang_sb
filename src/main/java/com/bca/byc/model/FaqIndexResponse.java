package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FaqIndexResponse extends AdminModelBaseDTOResponse {

    private String name;
    private String question;
    private String answer;
    private String status;
    private Integer orders;

}
