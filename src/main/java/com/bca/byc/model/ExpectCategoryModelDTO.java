package com.bca.byc.model;


import com.bca.byc.entity.ExpectItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExpectCategoryModelDTO {
    @Data
    public static class ExpectCategoryDetailResponse implements Serializable {

        private Long id;
        private String name;
        private String description;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;

        private List<ExpectItemModelDTO.ExpectItemDetailResponse> expectItems = new ArrayList<>();


    }

    @Data
    public static class ExpectCategoryCreateRequest {

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
    public static class ExpectCategoryUpdateRequest {

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
