package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountIndexResponse extends AdminModelBaseDTOResponse<Long>{

    private String name;
    private Boolean status;
    private Set<String> channelNames = new HashSet<>();

}
