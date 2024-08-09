package com.bca.byc.model.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Breadcrumb {
    private String name;
    private String route;
    private boolean current;

    // Constructors, getters, and setters
}