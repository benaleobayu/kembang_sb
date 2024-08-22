package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class AdminModelDTO {
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminDetailResponse {

        private Long id;
        private String name;
        private String role;
        private String email;
        private boolean status;
        private String createdAt;
        private String updatedAt;

        public String getRole() {
            if (role != null && role.contains("name=")) {
                return role.substring(role.indexOf("name=") + 5, role.length() - 1);
            }
            return role;
        }

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminCreateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;


    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String email;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }


}
