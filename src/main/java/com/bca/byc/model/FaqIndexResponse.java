package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FaqIndexResponse extends AdminModelBaseDTOResponse {

    private String name;

}
