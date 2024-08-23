package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


public class ExpectItemModelDTO {
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExpectItemDetailResponse implements Serializable {

        private Long id;
        private String name;
        private String description;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExpectItemCreateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

        // relation
        @NotBlank(message = "Expect Category Id is mandatory")
        private Long expectCategoryId;


    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ExpectItemUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }


}
