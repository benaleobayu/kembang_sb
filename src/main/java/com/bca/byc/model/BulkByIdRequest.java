package com.bca.byc.model;

import lombok.Data;

import java.util.Set;

@Data
public class BulkByIdRequest {

    private Set<Long> ids;

}
