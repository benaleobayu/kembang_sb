package com.kembang.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CastIdBySecureIdProjection {

    private Long id;

    private String secureId;

}
