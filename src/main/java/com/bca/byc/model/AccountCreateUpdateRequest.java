package com.bca.byc.model;

import lombok.Data;

import java.util.Set;

@Data
public class AccountCreateUpdateRequest {
    private String name;
    private Boolean status;
    private Set<String> channelIds;
}
