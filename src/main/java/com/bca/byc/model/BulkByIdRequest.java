package com.bca.byc.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class BulkByIdRequest {

    @NotNull(message = "ids cannot be null")
    private Set<String> ids;

}
