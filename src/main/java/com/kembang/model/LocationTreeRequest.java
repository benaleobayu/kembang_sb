package com.kembang.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationTreeRequest {
    @NotBlank(message = "Location is required")
    private String locationId;
}
