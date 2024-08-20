package com.bca.byc.model.cms;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

public class SettingsModelDTO {

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DetailResponse implements Serializable {

        @Serial
        private final static long serialVersionUID = -4110836650683334939L;

        private Long id;
        private String name;
        private String identity;
        private String description_id;
        private String description_en;
        private Integer value;
        private Boolean status;
        private String createdAt;
        private String updatedAt;

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        @NotBlank(message = "Identity is mandatory")
        @Size(max = 50, message = "Identity must be less than 50 characters")
        private String identity;

        private String description_id;
        private String description_en;
        private Integer value;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateRequest {

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
