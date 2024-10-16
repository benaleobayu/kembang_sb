package com.bca.byc.model;

import com.bca.byc.entity.Faq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FaqDetailResponse extends AdminModelBaseDTOResponse<Long> {
    private String name;
    private String question;
    private String answer;
    private Integer orders;
    private Boolean status;
}
