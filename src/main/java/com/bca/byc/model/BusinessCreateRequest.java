package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
    @AllArgsConstructor
    public class BusinessCreateRequest {

        @NotBlank(message = "user_id is required")
        private Long userId;

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        private String name;

        private String province;

        private String lineOfBusiness;

        private String address;

        private String website;

        private String description;

        @NotBlank(message = "Order is required")
        private Integer orders;

        @NotBlank(message = "Status is required")
        private Boolean status;

        // many to many
        private List<Long> categoryIds;

    }
