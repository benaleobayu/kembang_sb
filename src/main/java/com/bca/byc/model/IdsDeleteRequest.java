package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class IdsDeleteRequest {
    private List<String> ids;
}
