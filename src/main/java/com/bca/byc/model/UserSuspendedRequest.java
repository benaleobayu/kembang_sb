package com.bca.byc.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserSuspendedRequest {

    private Set<Long> ids;

}
