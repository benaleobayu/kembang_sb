package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SettingsModelDTO {

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SettingsDetailResponse {

        private Long id;
        private String name;
        private String identity;
        private String description;
        private Integer value;
        private Boolean status;
        private String createdAt;
        private String updatedAt;

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SettingsCreateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        @NotBlank(message = "Identity is mandatory")
        @Size(max = 50, message = "Identity must be less than 50 characters")
        private String identity;

        private String description;
        private Integer value;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SettingsUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

//        @NotBlank(message = "Identity is mandatory")
//        @Size(max = 50, message = "Identity must be less than 50 characters")
//        private String identity;

        private String description;
        private Integer value;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }
}
