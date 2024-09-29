package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KanwilCreateUpdateRequest {

    @Schema(example = "KC01")
    private String code;

    @Schema(example = "Kanwil 1")
    private String name;

    @Schema(example = "Jl. Kanwil No. 1")
    private String address;

    @Schema(example = "08123456789")
    private String phone;

    @Schema(example = "1")
    private Long location;

    @Schema(example = "true | false")
    private Boolean status;

}
